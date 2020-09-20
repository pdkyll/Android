package com.androidhuman.example.simplegithub.ui.signin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.androidhuman.example.simplegithub.BuildConfig
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.api.AuthApi
import com.androidhuman.example.simplegithub.api.model.GithubAccessToken
import com.androidhuman.example.simplegithub.api.provideAuthApi
import com.androidhuman.example.simplegithub.data.AuthTokenProvider
import com.androidhuman.example.simplegithub.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.newTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    // Lazy 프로퍼티를 사용하기 위해 변수(var)에서 값(val)로 바꾼 후 사용합니다.
    internal val api by lazy { provideAuthApi() }

    internal val authTokenProvider by lazy { AuthTokenProvider(this) }

    // lateinit 은 프로퍼티의 초기화를 객체 생성 시점에 해주지 못하는 경우에만 '피치 못하게' 사용하는 키워드이므로,
    // 코틀린에서 lateinit 으로 선언된 프로퍼티는 항상 널이 아닌 프로퍼티로 간주합니다.
    // 즉, 이 프로퍼티를 사용하기 전에 초기화를 수행하지 않았을 때 컴파일 수준에서 이를 확인할 방법이 없습니다.
    // 따라서 이런 특성을 갖는 프로퍼티는 lateinit 보다는 명시적으로 널 값을 허용하도록 선언해 주는 것이 더 안전합니다.
    // 이렇게 하면 컴파일 단계에서 프로퍼티의 널 여부를 명시적으로 확인할 수 있게 되므로,
    // 널 여부에 따라 추가 작업을 수행하기에도 더욱 용이해집니다.
//    internal lateinit var accessTokenCall: Call<GithubAccessToken>

    // 널 값을 허용하도록 한 후, 초깃값을 명시적으로 null 로 지정합니다.
    internal var accessTokenCall: Call<GithubAccessToken>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        /*
         * 1. 버튼 클릭.
         * 버튼 초기화 과정.
         * 버튼을 누르면 Github 사용자 인증 웹페이지로 이동하게된다.
         * 웹페이지 주소는 Github 애플리케이션의 Client ID를 넣어준다.
         * 웹피이지는 크롬 커스텀 탭을 사용하여 표시한다.
         */
        // 인스턴스 선언 없이 뷰 ID 를 사용하여 인스턴스에 접근합니다.
        btnActivitySignInStart.setOnClickListener {
            val authUri = Uri.Builder().scheme("https").authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
                    .build()
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this@SignInActivity, authUri)
        }

        /*
         * authTokenProvider 를 사용하여 사용자 인증 토큰이 있는지 여부를 확인하고,
         * 만약 인증 토큰이 있다면 메인 액티비티로 이동한다.
         */
        if (null != authTokenProvider.token) {
            launchMainActivity()
        }
    }

    /*
     * 2. 액세스 토큰 요청
     * 버튼을 눌러 웹페이지에서 사용자 인증이 완료되면
     * simplegithub://authrize?code={액세스 토큰 교환용 코드} 형태의 주소로 페이지가 리디렉션됩니다.
     * 이때 SignInActivity 가 이 형태의 주소를 열 수 있도록 매니페스트에 선언했으므로 SignInActivity 가 열리며,
     * 액티비티가 열리는 시점에 SignInActivity 가 화면에 표시되고 있는 상태이므로 onCreate() 콜백 대신
     * onNewIntent() 콜백이 호출됩니다.
     * 이때, 인텐트 내 data 필드에는 이 액티비티를 호출하기 위해 사용한 주소가 들어 있으며,
     * 이 주소에는 액세스 토큰과 교환할 수 있는 코드가 쿼리 매개변수(query parameter) 형태로 포함되어 있습니다.
     * 따라서 코드를 주소에서 추출하여 액세스 토큰을 요청합니다.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        showProgress()

        // 엘비스 연산자를 사용하여 널 값을 검사합니다.
        // intent.data 가 널이라면 IllegalArgumentException 예외를 발생시킵니다.
        val uri = intent.data ?: throw IllegalArgumentException("No data exists")

        // 엘비스 연산자를 사용하여 널 값을 검사합니다.
        // uri.getQueryParameter("code") 반환값이 널이라면
        // IllegalStateException 예외를 발생시킵니다.
        val code = uri.getQueryParameter("code") ?: throw IllegalStateException("No code exists")
        getAccessToken(code)
    }

    override fun onStop() {
        super.onStop()
        // 액티비티가 화면에서 사라지는 시점에 API 호출 객체가 생성되어 있다면
        // API 요청을 취소합니다.
        accessTokenCall?.run { cancel() }
    }

    /*
     * 3. 액세스 토큰 추출
     * 액세스 토큰은 REST API 를 사용하여 발급받으며, Github 애플리케이션의 Client ID 와 Client Secret,
     * 그리고 앞에서 사용자 인증이 완료된 후 받은 코드를 함께 전달합니다.
     * 액세스 토큰을 정상적으로 발급받았다면 이후 이를 계속 사용할 수 있도록 저장한 후 메인 액티비티로 이동합니다.
     */
    private fun getAccessToken(code: String) {
        showProgress()
        accessTokenCall = api.getAccessToken(
                BuildConfig.GITHUB_CLIENT_ID, BuildConfig.GITHUB_CLIENT_SECRET, code)
        // 앞에서 API 호출에 필요한 객체를 받았으므로,
        // 이 시점에서 accessTokenCall 객체의 값은 널이 아닙니다.
        // 따라서 비 널 값 보증(!!)을 사용하여 이 객체를 사용합니다.
        // Call 이너페이스를 구현하는 익명 클래스의 인스턴스르 생성합니다.
        accessTokenCall!!.enqueue(object : Callback<GithubAccessToken?> {
            override fun onResponse(call: Call<GithubAccessToken?>,
                                    response: Response<GithubAccessToken?>) {
                hideProgress()
                val token = response.body()
                if (response.isSuccessful && null != token) {
                    authTokenProvider.updateToken(token.accessToken)
                    launchMainActivity()
                } else {
                    showError(IllegalStateException(
                            "Not successful: " + response.message()))
                }
            }

            override fun onFailure(call: Call<GithubAccessToken?>, t: Throwable) {
                hideProgress()
                showError(t)
            }
        })
    }

    private fun showProgress() {
        btnActivitySignInStart.visibility = View.GONE
        pbActivitySignIn.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        btnActivitySignInStart.visibility = View.VISIBLE
        pbActivitySignIn.visibility = View.GONE
    }

    private fun showError(throwable: Throwable) {
        // 긴 시간 동안 표시되는 토스트 메시지를 출력합니다.
        longToast(throwable.message ?: "No message available")
    }

    private fun launchMainActivity() {
        startActivity(intentFor<MainActivity>().clearTask().newTask())
    }
}