package com.androidhuman.example.simplegithub.ui.repo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.api.provideGithubApi
import com.androidhuman.example.simplegithub.extensions.plusAssign
import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import com.androidhuman.example.simplegithub.ui.GlideApp
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_repository.*
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
//    internal var repoCall: Call<GithubRepo>? = null

    // 여러 디스포저블 객체를 관리할 수 있는 CompositeDisposable 객체를 초기화합니다.
    // CompositeDisposable 에서 AutoClearedDisposable 로 변경합니다.
    internal val disposables = AutoClearedDisposable(this)

    // 액티비티가 완전히 종료되기 전까지 이벤트를 계속 받기 위해 추가합니다.
    internal val viewDisposables = AutoClearedDisposable(lifecycleOwner = this, alwaysClearOnStop = false)

    // RepositoryViewModel 을 생성하기 위해 필요한 뷰모델 팩토리 클래스의 인스턴스를 생성합니다.
    internal val viewModelFactory by lazy {
        RepositoryViewModelFactory(provideGithubApi(this))
    }

    // 뷰모델의 인스턴스는 onCreate() 에서 받으므로, lateinit 으로 선언합니다
    lateinit var viewModel: RepositoryViewModel

    // REST API 응답에 포함된 날짜 및 시간 표시 형식입니다.
    internal val dateFormatInResponse = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())

    // 화면에서 사용자에게 보여줄 날짜 및 시간 표시 형식입니다.
    internal val dateFormatToShow = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        // RepositoryViewModel 의 인스턴스를 받습니다.
        viewModel = ViewModelProvider(this, viewModelFactory)[RepositoryViewModel::class.java]

        // Lifecycle.addObserver() 함수를 사용하여
        // AutoClearedDisposable 객체를 옵저버로 등록합니다.
        lifecycle += disposables

        // viewDisposables 에서 이 액티비티의 생명주기 이벤트를 받도록 합니다.
        lifecycle += viewDisposables

        // 저장소 정보 이벤트만 받도록 합니다.
        viewDisposables += viewModel.repository

                // 유효한 저장소 이벤트만 받도록 합니다.
                .filter { !it.isEmpty }
                .map { it.value }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repository ->
                    GlideApp.with(this@RepositoryActivity)
                            .load(repository.owner.avatarUrl)
                            .into(ivActivityRepositoryProfile)

                    tvActivityRepositoryName.text = repository.fullName
                    tvActivityRepositoryStars.text = resources
                            .getQuantityString(R.plurals.star, repository.stars, repository.stars)
                    if (null == repository.description) {
                        tvActivityRepositoryDescription.setText(R.string.no_description_provided)
                    } else {
                        tvActivityRepositoryDescription.text = repository.description
                    }
                    if (null == repository.language) {
                        tvActivityRepositoryLanguage.setText(R.string.no_language_specified)
                    } else {
                        tvActivityRepositoryLanguage.text = repository.language
                    }

                    try {
                        val lastUpdate = dateFormatInResponse.parse(repository.updatedAt)
                        tvActivityRepositoryLastUpdate.text = dateFormatToShow.format(lastUpdate)
                    } catch (e: ParseException) {
                        tvActivityRepositoryLastUpdate.text = getString(R.string.unknown)
                    }
                }

        // 메시지 이벤트를 구독합니다
        viewDisposables += viewModel.message
                .observeOn(AndroidSchedulers.mainThread())

                // 메시지 이벤트를 받으면 화면에 해당 메시지를 표시합니다.
                .subscribe { message -> showError(message) }

        // 저장소 정보를 보여주는 뷰의 표시 유무를 결정하는 이벤트를 구독합니다.
        viewDisposables += viewModel.isContentVisible
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { visible -> setContentVisibility(visible) }

        // 작업 진행 여부 이벤트를 구독합니다.
        viewDisposables += viewModel.isLoading
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isLoading ->
                    // 작업 진행 여부 이벤트에 따라 프로그레스바의 표시 상태를 변경합니다.
                    if (isLoading) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }

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

        // ViewModel 패턴 추가로 주석 처리.
//        showRepositoryInfo(login, repo)

        // 저장소 정보를 요청합니다.
        disposables += viewModel.requestRepositoryInfo(login, repo)
    }

    // onStop() 함수는 더 이상 오버라이드하지 않아도 됩니다.
//    override fun onStop() {
//        super.onStop()
//        // 액티비티가 화면에서 사라지는 시점에 API 호출 객체가 생성되어 있다면
//        // API 요청을 취소합니다.
////        repoCall?.run { cancel() }
//
//        // 관리하고 있던 디스포저블 객체를 모두 해제합니다.
//        disposable.clear()
//    }

    private fun showRepositoryInfo(login: String, repoName: String) {
        // Retrofit Call 객체를 사용하였을 때 아래와 같이 사용하였다.
        /*showProgress()

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
        })*/

        // REST API 를 통해 저장소 정보를 요청합니다.
        // '+=' 연산자로 디스포저블을 CompositeDisposable 에 추가합니다.
        // ViewModel 패턴 추가로 주석 처리.
//        disposables += api.getRepository(login, repoName)
//                // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
//                .observeOn(AndroidSchedulers.mainThread())
//
//                // 구독할 때 수행할 작업을 구현합니다.
//                .doOnSubscribe { showProgress() }
//
//                // 에러가 발생했을 때 수행할 작업을 구현합니다.
//                .doOnError { hideProgress(false) }
//
//                // 스트림이 정상 종료되었을 때 수행할 작업을 구현합니다.
//                .doOnComplete { hideProgress(true) }
//
//                // 옵저버블을 구독합니다.
//                .subscribe({ repo ->
//                    // API 를 통해 저장소 정보를 정상적으로 받았을 때 처리할 작업을 구현합니다.
//                    // 작업 중 오류가 발생하면 이 블록은 호출되지 않씁니다.
//
//                    // 저장소 소유자의 프로필 사진을 표시합니다.
//                    GlideApp.with(this@RepositoryActivity)
//                            .load(repo.owner.avatarUrl)
//                            .into(ivActivityRepositoryProfile)
//
//                    // 저장소 정보를 표시합니다.
//                    tvActivityRepositoryName.text = repo.fullName
//                    tvActivityRepositoryStars.text = resources
//                            .getQuantityString(R.plurals.star, repo.stars, repo.stars)
//                    if (null == repo.description) {
//                        tvActivityRepositoryDescription.setText(R.string.no_description_provided)
//                    } else {
//                        tvActivityRepositoryDescription.text = repo.description
//                    }
//                    if (null == repo.language) {
//                        tvActivityRepositoryLanguage.setText(R.string.no_language_specified)
//                    } else {
//                        tvActivityRepositoryLanguage.text = repo.language
//                    }
//                    try {
//                        // 응답에 포함된 마지막 업데이트 시각을 Date 형식으로 변환합니다.
//                        val lastUpdate = dateFormatInResponse.parse(repo.updatedAt)
//
//                        // 마지막 업데이트 시각을 yyyy-MM-dd HH:mm:ss 형태로 표시합니다.
//                        tvActivityRepositoryLastUpdate.text = dateFormatToShow.format(lastUpdate)
//                    } catch (e: ParseException) {
//                        tvActivityRepositoryLastUpdate.text = getString(R.string.unknown)
//                    }
//                }) {
//                    // 에러 블록
//                    // 네트워크 오류나 데이터 처리 오류 등
//                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
//                    showError(it.message)
//                }

    }

    private fun showProgress() {
        pbActivityRepository.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        pbActivityRepository.visibility = View.GONE
    }

    private fun setContentVisibility(show: Boolean) {
        llActivityRepositoryContent.visibility = if (show) View.VISIBLE else View.GONE
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