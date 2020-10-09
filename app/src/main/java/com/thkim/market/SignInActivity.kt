package com.thkim.market

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 101

        private const val TAG_Thkim = "Thkim_SignInActivity"
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG_Thkim, "onCreate()")
        setContentView(R.layout.activity_sign_in)

        // Configure Google Sign In
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
//            .requestScopes() // request additional scopes to access Google APIs
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions)

        // Initialize Firebase Auth
//        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener {
            Log.d(TAG_Thkim, "onClick()")
            when (it.id) {
                R.id.sign_in_button -> {
                    Log.d(TAG_Thkim, "sign_in_button clicked")
                    signIn()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG_Thkim, "onStart()")

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn() {
        Log.d(TAG_Thkim, "signIn()")
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(account: Any? = null) {
        Log.d(TAG_Thkim, "updateUI()")
        if (account is GoogleSignInAccount) {
            // 새로 로그인 하는 경우
            Snackbar.make(sign_in_layout as View, "New Login.", Snackbar.LENGTH_SHORT).show()
        } else if (account is FirebaseUser) {
            // 이전에 로그인을 했던 이력이 있는 경우
            Snackbar.make(sign_in_layout as View, "Already Login information is exists.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        Log.d(TAG_Thkim, "handleSignInResult()")
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG_Thkim, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG_Thkim, "firebaseAuthWithGoogle()")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG_Thkim, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_Thkim, "signInWithCredential:failure", task.exception)
                    // ...
                    Snackbar.make(sign_in_layout as View, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG_Thkim, "onActivityResult()")

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG_Thkim, "firebaseAuthWithGoogle id :" + account.id)
                Log.d(TAG_Thkim, "firebaseAuthWithGoogle idToken :" + account.idToken)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_Thkim, "Google sign in failed", e)
                // ...
            }
        }
    }
}