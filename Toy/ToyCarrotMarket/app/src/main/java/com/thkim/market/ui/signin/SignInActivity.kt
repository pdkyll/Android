package com.thkim.market.ui.signin

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.thkim.market.R
import com.thkim.market.api.SignInApi
import com.thkim.market.rx.AutoClearedDisposable
import com.thkim.market.ui.main.MainActivity
import com.thkim.market.util.DLog
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.newTask
import javax.inject.Inject


class SignInActivity : DaggerAppCompatActivity() {
    companion object {
        private const val TAG_Thkim = "Thkim_SignInActivity"

        const val RC_SIGN_IN = 101
    }

    private val auth = Singleton.getInstance()

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val disposable = AutoClearedDisposable(this)

    private val viewDisposable = AutoClearedDisposable(this, false)

    @Inject
    lateinit var viewModelFactory: SignInViewModelFactory

    @Inject
    lateinit var signInApi: SignInApi

    private lateinit var viewModel: SignInViewModel

    private var isConnected: Boolean = false

    object Singleton {
        fun getInstance() = FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DLog.start()
        setContentView(R.layout.activity_sign_in)

        viewModel = ViewModelProvider(this, viewModelFactory)[SignInViewModel::class.java]

        lifecycle.addObserver(disposable)

        lifecycle.addObserver(viewDisposable)

        sign_in_button.setOnClickListener {
            // Check network connection
            if (isConnected) {
                DLog.d("Network is connected -> isConnected : $isConnected")
                // Internet Connected

                // Configure Google Sign In
                val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                // Build a GoogleSignInClient with the options specified by gso.
                mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions)

                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            } else {
                DLog.d("Network is connected -> isConnected : $isConnected")
                // Not Connected
                Snackbar.make(
                    sign_in_layout as View,
                    "Please check network state.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewDisposable.add(viewModel.currentUser
            .filter {
                !it.isEmpty
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { launchMainActivity() })

        viewDisposable.add(viewModel.message
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showError(it)
            })

        viewDisposable.add(viewModel.isLoading
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isLoading ->
                if (isLoading) {
                    showProgress()
                } else {
                    hideProgress()
                }
            })

        disposable.add(viewModel.isConnected
            .subscribe {
                isConnected = it
            })

        checkNetState()
        DLog.end()
    }

    override fun onStart() {
        super.onStart()
        DLog.start()

        // Check if user is signed in (non-null) and update UI accordingly.
        if (auth.currentUser != null) {
            launchMainActivity()
        }
        DLog.end()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DLog.start()

        showProgress()
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            DLog.d("RC_SIGN_IN")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                getSignInAccount(task)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                DLog.w("Google sign in failed -> $e")
                Snackbar.make(
                    sign_in_layout as View,
                    "Google sign in failed.",
                    Snackbar.LENGTH_SHORT
                ).show()
                hideProgress()
            }
        }
        DLog.end()
    }

    private fun getSignInAccount(task: Task<GoogleSignInAccount>) {
        DLog.start()
        // Google Sign In was successful, authenticate with Firebase
        // 아래에서 에러가 발생할 경우 개발 PC의 SHA-1 지문을 Firebase 콘솔에 추가해야한다.
        val account = task.getResult(ApiException::class.java)!!
        disposable.add(viewModel.requestSignInAccount(account.idToken!!, auth))
        DLog.end()
    }

    @Suppress("DEPRECATION")
    private fun checkNetState() {
        DLog.start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            disposable.add(viewModel.getNetworkState(applicationContext))
        } else {
            DLog.d("LOW SDK VERSION : ${Build.VERSION.SDK_INT}")
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: android.net.NetworkInfo? = cm.activeNetworkInfo
            isConnected = activeNetwork?.isConnectedOrConnecting == true
        }
        DLog.end()
    }

    private fun showProgress() {
        sign_in_button.visibility = View.GONE
        pbActivitySignIn.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        sign_in_button.visibility = View.VISIBLE
        pbActivitySignIn.visibility = View.GONE
    }

    private fun showError(message: String?) {
        // 긴 시간 동안 표시되는 토스트 메시지를 출력합니다.
        longToast(message ?: "No message available")
    }

    private fun launchMainActivity() {
        startActivity(intentFor<MainActivity>().clearTask().newTask())
    }
}