package com.thkim.mediapipelab.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

/*
 * Created by Thkim on 2020/09/22
 */
interface DownloadApi {

    @GET("ssdlite_object_detection.tflite")
    fun downloadTest(): Call<ResponseBody>

    /* ----------- MediaPipe Model Files ----------- */
    // Object detection model
    @GET("ssdlite_object_detection.tflite")
    fun downloadOTModel(): Observable<ResponseBody>

    @GET("ssdlite_object_detection_labelmap.txt")
    fun downloadOTFile(): Observable<ResponseBody>

    // Face model
    @GET("face_detection_front.tflite")
    fun downloadFaceModel(): Observable<ResponseBody>

    @GET("face_detection_front_labelmap.txt")
    fun downloadFaceFile(): Observable<ResponseBody>

    // Face landmark
    @GET("face_landmark.tflite")
    fun downloadFaceLandmarkModel(): Observable<ResponseBody>

    // HairSegmentation Model
    @GET("hair_segmentation.tflite")
    fun downloadHairSegModel(): Observable<ResponseBody>

    // PoseDetection
    @GET("pose_detection.tflite")
    fun downloadPoseModel(): Observable<ResponseBody>

    // Pose landmark
    @GET("pose_landmark_upper_body.tflite")
    fun downloadPoseLandmarkModel(): Observable<ResponseBody>

    /* ----------- MediaPipe Jni Files ----------- */
    @GET("arm64-v8a/libmediapipe_jni.so")
    fun downloadMPJniFor64(): Observable<ResponseBody>

    @GET("arm64-v8a/libopencv_java3.so")
    fun downloadOpenCVJniFor64(): Observable<ResponseBody>

    @GET("armeabi-v7a/libmediapipe_jni.so")
    fun downloadMPJniFor32(): Observable<ResponseBody>

    @GET("armeabi-v7a/libopencv_java3.so")
    fun downloadOpenCVJniFor32(): Observable<ResponseBody>
}