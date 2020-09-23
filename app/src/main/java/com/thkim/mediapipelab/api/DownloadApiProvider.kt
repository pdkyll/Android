package com.thkim.mediapipelab.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/*
 * Created by Thkim on 2020/09/22
 */

fun provideModelDownApi(): DownloadApi = Retrofit.Builder()
        .baseUrl("https://www.camerafi.com/live/mediapipe/model/")
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        // 받은 응답을 옵저버블 형태로 반환해주도록 합니다.
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .build()
        .create(DownloadApi::class.java)

fun provideJniDownApi(): DownloadApi = Retrofit.Builder()
        .baseUrl("https://www.camerafi.com/live/mediapipe/jni/")
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        // 받은 응답을 옵저버블 형태로 반환해주도록 합니다.
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .build()
        .create(DownloadApi::class.java)

// 네트워크 통신에 사용할 클라이언트 객체를 생성합니다.
private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        // run() 함수로 OkHttpClient.Builder 변수 선언을 제거합니다.
        .run {
            addInterceptor(interceptor)
            build()
        }

// 네트워크 요청/응답을 로그에 표시하는 Interceptor 객체를 생성합니다.
private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }