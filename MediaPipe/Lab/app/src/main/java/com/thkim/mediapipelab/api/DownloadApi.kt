package com.thkim.mediapipelab.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

/*
 * Created by Thkim on 2020/09/22
 */
interface DownloadApi {

    @Streaming
    @GET("{model}")
    fun downloadModel(@Path("model") model: String): Observable<ResponseBody>

    @Streaming
    @GET("arm64-v8a/{lib_name}")
    fun downloadJniFor64(@Path("lib_name") lib_name: String): Observable<ResponseBody>

    @Streaming
    @GET("armeabi-v7a/{lib_name}")
    fun downloadJniFor32(@Path("lib_name") lib_name: String): Observable<ResponseBody>


    /* ----------- MediaPipe Model Files ----------- */
    // Object detection model
    /* ----------- MediaPipe Jni Files ----------- */
//    @GET("arm64-v8a/libmediapipe_jni.so")
//    fun downloadMPJniFor64(): Observable<ResponseBody>
//
//    @GET("arm64-v8a/libopencv_java3.so")
//    fun downloadOpenCVJniFor64(): Observable<ResponseBody>
//
//    @GET("armeabi-v7a/libmediapipe_jni.so")
//    fun downloadMPJniFor32(): Observable<ResponseBody>
//
//    @GET("armeabi-v7a/libopencv_java3.so")
//    fun downloadOpenCVJniFor32(): Observable<ResponseBody>
}