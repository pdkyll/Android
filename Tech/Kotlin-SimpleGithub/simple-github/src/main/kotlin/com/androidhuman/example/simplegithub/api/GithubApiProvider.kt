package com.androidhuman.example.simplegithub.api

import android.content.Context
import com.androidhuman.example.simplegithub.data.AuthTokenProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.jvm.Throws

/*
 * Retrofit 을 통해 REST API 를 호출하려면 다음과 같은 요소가 필요합니다.
 * 호스트 서버 주소
 * 네트워크 통신에 사용할 클라이언트 구현
 * REST API 응답을 변환할 컨버터
 * REST API 가 정의된 인터페이스
 */

/*
 * Retrofit 에서 받은 응답을 옵저버블 형태로 반환하도록 하려면 두 가지 작업이 필요합니다.
 * 2. 앞에서 정의한 API 를 호출할 수 있는 객체를 만들어주는 GithubApiProvider.kt 를 수정해야합니다.
 *    Retrofit 에서 받은 응답을 옵저버블 형태로 변경해주는 RxJava2CallAdapterFactory 를 API 의 콜 어댑터로 추가하며,
 *    비동기 방식으로 API 를 호출하도록 RxJava2CallAdapterFactory.createAsync() 메서드로 콜 어댑터를 생성합니다.
 */

// 함수들이 싱글톤 함수 내부에 있었으나 패키지 단위 함수로 변환하였다.

// 액세스 토큰 획득을 위한 객체를 생성합니다.
fun provideAuthApi(): AuthApi = Retrofit.Builder()
        .baseUrl("https://github.com/")
        .client(provideOkHttpClient(provideLoggingInterceptor(), null))
        // 받은 응답을 옵저버블 형태로 반환해주도록 합니다.
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)


// 저장소 정보에 접근하기 위한 객체를 생성합니다.
fun provideGithubApi(context: Context): GithubApi = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(provideOkHttpClient(provideLoggingInterceptor(),
                provideAuthInterceptor(provideAuthTokenProvider(context))))
        // 받은 응답을 옵저버블 형태로 반환하며,
        // 비동기 방식으로 API 를 호출합니다.
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApi::class.java)


// 네트워크 통신에 사용할 클라이언트 객체를 생성합니다.
private fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor?): OkHttpClient = OkHttpClient.Builder()
        // run() 함수로 OkHttpClient.Builder 변수 선언을 제거합니다.
        .run {
            if (null != authInterceptor) {
                addInterceptor(authInterceptor)
            }
            addInterceptor(interceptor)
            build()
        }


// 네트워크 요청/응답을 로그에 표시하는 Interceptor 객체를 생성합니다.
private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

// 액세스 토큰을 헤더에 추가하는 Interceptor 객체를 생성합니다.
private fun provideAuthInterceptor(provider: AuthTokenProvider): AuthInterceptor {
    val token = provider.token ?: throw IllegalStateException("authToken cannot be null.")
    return AuthInterceptor(token)
}

private fun provideAuthTokenProvider(context: Context): AuthTokenProvider = AuthTokenProvider(context.applicationContext)


internal class AuthInterceptor(private val token: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain)
    // with() 함수와 run() 함수로 추가 변수 선언을 제거합니다.
            : Response = with(chain) {
        val newRequest = request().newBuilder().run {
            addHeader("Authorization", "token" + token)
            build()
        }
        proceed(newRequest)
    }

}
