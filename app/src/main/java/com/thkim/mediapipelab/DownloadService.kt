package com.thkim.mediapipelab

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.thkim.mediapipelab.api.provideJniDownApi
import com.thkim.mediapipelab.api.provideModelDownApi
import com.thkim.mediapipelab.rx.AutoClearedDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*

/*
 * Created by Thkim on 2020/09/22
 */
class DownloadService(private val activity: AppCompatActivity) {

    companion object {
        private const val TAG: String = "DownloadService"

        const val objectTrackingModel: String = "ssdlite_object_detection.tflite"
        const val objectTrackingLabel: String = "ssdlite_object_detection_labelmap.txt"

        const val faceDetectionModel: String = "face_detection_front.tflite"
        const val faceDetectionLabel: String = "face_detection_labelmap.txt"

        const val faceMeshModel: String = "face_landmark.tflite"

        const val hairSegmentationModel: String = "hair_segmentation.tflite"

        const val poseDetectionModel: String = "pose_detection.tflite"

        const val poseLandmarkModel: String = "pose_landmark_upper_body.tflite"
    }


    private val mpJniName: String = "libmediapipe_jni.so"
    private val openCVJniName: String = "libopencv_java3.so"

    internal val modelApi by lazy { provideModelDownApi() }

    internal val jniApi by lazy { provideJniDownApi() }

    internal val disposable = AutoClearedDisposable(activity)

    lateinit var downloadListener: DownloadListener

    init {
        activity.lifecycle.addObserver(disposable)
    }

    fun onItemDownloadedListener(downloadListener: DownloadListener) {
        this@DownloadService.downloadListener = downloadListener
    }

    fun downloadModel(model: String, label: String? = null) {
        disposable.add(modelApi.downloadModel(model)
                .doOnSubscribe { }
                .doOnComplete { }
                .observeOn(Schedulers.io())
                .subscribe({ body ->
                    writeResponseBody(body, model)
                }) {
                    it.message
                })

        if (label != null) {
            disposable.add(modelApi.downloadModel(label)
                    .observeOn(Schedulers.io())
                    .subscribe({ body ->
                        writeResponseBody(body, label)
                    }) {
                        it.message
                    })
        }
    }

    fun downloadJniFor64() {
        disposable.add(jniApi.downloadJniFor64(mpJniName)
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { downloadListener.itemDownloadedListener() }
                .subscribe({ body ->
                    writeResponseBody(body, mpJniName)
                }) {
                    Log.d(TAG, "Fail to download mp jni for 64 file.")
                })

        disposable.add(jniApi.downloadJniFor64(openCVJniName)
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { downloadListener.itemDownloadedListener() }
                .subscribe({ body ->
                    writeResponseBody(body, openCVJniName)
                }) {
                    Log.d(TAG, "Fail to download mp jni for 64 file.")
                })
    }

    fun downloadJniFor32() {
        disposable.add(jniApi.downloadJniFor32(mpJniName)
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { downloadListener.itemDownloadedListener() }
                .subscribe({ body ->
                    writeResponseBody(body, mpJniName)
                }) {
                    Log.d(TAG, "Fail to download openCV jni for 32 file.")
                })

        disposable.add(jniApi.downloadJniFor32(openCVJniName)
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { downloadListener.itemDownloadedListener() }
                .subscribe({ body ->
                    writeResponseBody(body, openCVJniName)
                }) {
                    Log.d(TAG, "Fail to download openCV jni for 32 file.")
                })
    }

    private fun writeResponseBody(body: ResponseBody, fileName: String?) {
        try {
            val modelFile = File(activity.cacheDir.absolutePath
                    + File.separator
                    + "/"
                    + fileName)
            Log.d(TAG, modelFile.toString())
            if (modelFile.exists()) {
                Log.d(TAG, "파일이 존재함.")
            } else {
                Log.d(TAG, "파일 없음.")
                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null

                try {
                    val fileReader = ByteArray(4096)
                    val fileSize = body.contentLength()
                    var fileSizeDownloaded: Long = 0

                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(modelFile)

                    while (true) {
                        val read = inputStream.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        outputStream.write(fileReader, 0, read)
                        fileSizeDownloaded += read.toLong()
                        Log.d(TAG, "File Download : $fileSizeDownloaded of $fileSize")
                        outputStream.flush()
                    }
                    return
                } catch (e: IOException) {
                    e.printStackTrace()
                    return
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
    }
}

interface DownloadListener {
    //    fun itemDownloadingListener()
    fun itemDownloadedListener()
}

abstract class DownloadManager {
    fun itemIsDone() {
        Log.d("TAG", "item is done")
    }
}