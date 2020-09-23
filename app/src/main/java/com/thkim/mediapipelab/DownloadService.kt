package com.thkim.mediapipelab

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.thkim.mediapipelab.api.provideJniDownApi
import com.thkim.mediapipelab.api.provideModelDownApi
import com.thkim.mediapipelab.rx.AutoClearedDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import java.io.*

/*
 * Created by Thkim on 2020/09/22
 */
class DownloadService(private val activity: AppCompatActivity) {

    companion object {
        private const val TAG: String = "DownloadService"
    }

    private val assetCacheName: String = "mediapipe_asset_cache"
    private val jniFolderName: String = "jni"
    private val arm64_v8a: String = "arm64-v8a"
    private val armeabi_v7a: String = "armeabi-v7a"

    private val modelName: String = "ssdlite_object_detection.tflite"
    private val modelTxt: String = "ssdlite_object_detection_labelmap.txt"

    private val mpJni: String = "libmediapipe_jni.so"
    private val openCVJni: String = "libopencv_java3.so"

    internal val modelApi by lazy { provideModelDownApi() }

    internal val jniApi by lazy { provideJniDownApi() }

    internal val disposable = AutoClearedDisposable(activity)

    init {
        activity.lifecycle.addObserver(disposable)
    }

    fun downloadModel() {
        disposable.add(modelApi.downloadOTModel()
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { }
                .subscribe({ body ->
                    writeResponseBody(body, modelName, assetCacheName)
                }) {
                    it.message
                    Log.d(TAG, "Fail to download .tflite model.")
                })

        disposable.add(modelApi.downloadOTFile()
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { }
                .subscribe({ body ->
                    writeResponseBody(body, modelTxt, assetCacheName)
                }) {
                    it.message
                    Log.d(TAG, "Fail to download .txt file.")
                })
    }

    fun downloadJniFor64() {
        disposable.add(jniApi.downloadMPJniFor64()
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { }
                .subscribe({ body ->
                    writeResponseBody(body, mpJni, arm64_v8a)
                }) {
                    Log.d(TAG, "Fail to download mp jni for 64 file.")
                })

        disposable.add(jniApi.downloadOpenCVJniFor64()
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { }
                .subscribe({ body ->
                    writeResponseBody(body, openCVJni, arm64_v8a)
                }) {
                    Log.d(TAG, "Fail to download mp jni for 64 file.")
                })
    }

    fun downloadJniFor32() {
        disposable.add(jniApi.downloadMPJniFor32()
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { }
                .subscribe({ body ->
                    writeResponseBody(body, mpJni, armeabi_v7a)
                }) {
                    Log.d(TAG, "Fail to download openCV jni for 32 file.")
                })

        disposable.add(jniApi.downloadOpenCVJniFor32()
                .observeOn(Schedulers.io())
                .doOnSubscribe { }
                .doOnComplete { }
                .subscribe({ body ->
                    writeResponseBody(body, openCVJni, armeabi_v7a)
                }) {
                    Log.d(TAG, "Fail to download openCV jni for 32 file.")
                })
    }


    private fun writeResponseBody(body: ResponseBody, fileName: String, dir: String) {
        try {
            makeDirectory(dir)
            val modelFile = File(activity.externalCacheDir.toString()
                    + File.separator
                    + dir
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

    private fun makeDirectory(folderName: String): Boolean {
        val dir = File(activity.externalCacheDir.toString() + File.separator + folderName)
        if (!dir.exists()) {
            return dir.mkdir()
        }
        return false
    }


}