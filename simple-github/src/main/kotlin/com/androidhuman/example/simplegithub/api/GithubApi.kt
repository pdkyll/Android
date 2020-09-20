package com.androidhuman.example.simplegithub.api

import com.androidhuman.example.simplegithub.api.model.GithubRepo
import com.androidhuman.example.simplegithub.api.model.RepoSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/*
 * Retrofit 에서 받은 응답을 옵저버블 형태로 반환하도록 하려면 두 가지 작업이 필요합니다.
 * 1. API 가 구현된 인터페이스에서 각 API 의 반환 형태를 Observable 로 바꿔야 합니다.
 * -> 진행 : AuthApi.kt, GitHubApi.kt
 */
interface GithubApi {
    @GET("search/repositories")
    fun searchRepository(
            @Query("q") query: String
    ): Observable<RepoSearchResponse> // 반환 타입을 Call -> Observable 로 변경.

    @GET("repos/{owner}/{name}")
    fun getRepository(
            @Path("owner") ownerLogin: String,
            @Path("name") repoName: String
    ): Observable<GithubRepo> // 반환 타입을 Call -> Observable 로 변경.
}