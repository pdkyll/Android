package com.androidhuman.example.simplegithub.api

import com.androidhuman.example.simplegithub.api.model.GithubAccessToken
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/*
 * Retrofit 에서 받은 응답을 옵저버블 형태로 반환하도록 하려면 두 가지 작업이 필요합니다.
 * 1. API 가 구현된 인터페이스에서 각 API 의 반환 형태를 Observable 로 바꿔야 합니다.
 * -> 진행 : AuthApi.kt, GitHubApi.kt
 */
interface AuthApi {

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("code") code: String): Observable<GithubAccessToken> // 반환 타입을 Call -> Observable 로 변경
}