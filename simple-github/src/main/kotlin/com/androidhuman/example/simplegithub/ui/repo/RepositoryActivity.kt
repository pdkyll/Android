package com.androidhuman.example.simplegithub.ui.repo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.api.model.GithubRepo
import com.androidhuman.example.simplegithub.api.provideGithubApi
import com.androidhuman.example.simplegithub.ui.GlideApp
import kotlinx.android.synthetic.main.activity_repository.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RepositoryActivity : AppCompatActivity() {

    // 정적 필드로 정의되어 있던 항목은 동반 객체 내부에 정의됩니다.
    companion object {
        const val KEY_USER_LOGIN = "user_login"

        const val KEY_REPO_NAME = "repo_name"
    }

    // lazy 프로퍼티로 전환합니다.
    internal val api by lazy { provideGithubApi(this) }

    // 널 값을 허용하도록 한 후, 초깃값을 명시적으로 null 로 지정합니다.
    internal var repoCall: Call<GithubRepo>? = null

    // REST API 응답에 포함된 날짜 및 시간 표시 형식입니다.
    internal val dateFormatInResponse = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())

    // 화면에서 사용자에게 보여줄 날짜 및 시간 표시 형식입니다.
    internal val dateFormatToShow = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        // 액티비티 호출 시 전달받은 사용자 이름과 저장소 이름을 추출합니다.
        // 엘비스 연산자를 사용하여 널 값을 검사합니다.
        // KEY_USER_LOGIN 이름으로 문자열 값이 포함되어 있지 않다면
        // IllegalArgumentException 예외를 발생시킵니다.
        val login = intent.getStringExtra(KEY_USER_LOGIN)
                ?: throw IllegalArgumentException("No login info exists in extras")

        // 엘비스 연산자를 사용하여 널 값을 검사합니다.
        // KEY_REPO_NAME 이름으로 문자열 값이 포함되어 있지 않다면
        // IllegalArgumentException 예외를 발생시킵니다.
        val repo = intent.getStringExtra(KEY_REPO_NAME)
                ?: throw IllegalArgumentException("No repo info exists in extras")
        showRepositoryInfo(login, repo)
    }

    override fun onStop() {
        super.onStop()
        // 액티비티가 화면에서 사라지는 시점에 API 호출 객체가 생성되어 있다면
        // API 요청을 취소합니다.
        repoCall?.run { cancel() }
    }

    private fun showRepositoryInfo(login: String, repoName: String) {
        showProgress()

        repoCall = api.getRepository(login, repoName)

        // 앞에서 API 호출에 필요한 객체를 받았으므로,
        // 이 시점에서 repoCall 객체의 값은 널이 아닙니다.
        // 따라서 비 널 값 보증(!!)을 사용하여 이 객체를 사용합니다.
        // Call 인터페이스를 구현하는 익명 클래스의 인스턴스를 생성합니다.
        repoCall!!.enqueue(object : Callback<GithubRepo?> {
            override fun onResponse(call: Call<GithubRepo?>, response: Response<GithubRepo?>) {
                hideProgress(true)

                val repo = response.body()
                if (response.isSuccessful && null != repo) {
                    // 저장소 소유자의 프로필 사진을 표시합니다.
                    GlideApp.with(this@RepositoryActivity)
                            .load(repo.owner.avatarUrl)
                            .into(ivActivityRepositoryProfile)

                    // 저장소 정보를 표시합니다.
                    tvActivityRepositoryName.text = repo.fullName
                    tvActivityRepositoryStars.text = resources
                            .getQuantityString(R.plurals.star, repo.stars, repo.stars)
                    if (null == repo.description) {
                        tvActivityRepositoryDescription.setText(R.string.no_description_provided)
                    } else {
                        tvActivityRepositoryDescription.text = repo.description
                    }
                    if (null == repo.language) {
                        tvActivityRepositoryLanguage.setText(R.string.no_language_specified)
                    } else {
                        tvActivityRepositoryLanguage.text = repo.language
                    }
                    try {
                        // 응답에 포함된 마지막 업데이트 시각을 Date 형식으로 변환합니다.
                        val lastUpdate = dateFormatInResponse.parse(repo.updatedAt)

                        // 마지막 업데이트 시각을 yyyy-MM-dd HH:mm:ss 형태로 표시합니다.
                        tvActivityRepositoryLastUpdate.text = dateFormatToShow.format(lastUpdate)
                    } catch (e: ParseException) {
                        tvActivityRepositoryLastUpdate.text = getString(R.string.unknown)
                    }
                } else {
                    showError("Not successful: " + response.message())
                }
            }

            override fun onFailure(call: Call<GithubRepo?>, t: Throwable) {
                hideProgress(false)
                showError(t.message)
            }
        })
    }

    private fun showProgress() {
        llActivityRepositoryContent.visibility = View.GONE
        pbActivityRepository.visibility = View.VISIBLE
    }

    private fun hideProgress(isSucceed: Boolean) {
        llActivityRepositoryContent.visibility = if (isSucceed) View.VISIBLE else View.GONE
        pbActivityRepository.visibility = View.GONE
    }

    private fun showError(message: String?) {
        // message 가 널 값인 경우 "Unexpected error." 메시지를 표시합니다.
        // with() 함수를 사용하여 tvActivityRepositoryMessage 범위 내에서 작업을 수행합니다.
        with(tvActivityRepositoryMessage) {
            text = message ?: "Unexpected error."
            visibility = View.VISIBLE
        }
    }

}