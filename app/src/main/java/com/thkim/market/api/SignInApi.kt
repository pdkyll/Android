package com.thkim.market.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.rxjava3.core.Single

/*
 * Created by Thkim on 2020/10/11
 */
class SignInApi {

    fun getAuthCredential(idToken: String): Single<AuthCredential> = Single.create {
        if (!it.isDisposed) {
            it.onSuccess(GoogleAuthProvider.getCredential(idToken, null))
        } else {
            it.onError(Throwable("SignInApi has error."))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun getNetworkState(context: Context): Single<Boolean> = Single.create {
        if (!it.isDisposed) {
            try {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//                val builder = NetworkRequest.Builder()
                connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        Log.d("Thkim_", "onAvailable() -> internet")
                        it.onSuccess(true)
                    }

                    override fun onLost(network: Network) {
                        Log.d("Thkim_", "onLost() -> internet")
                        it.onSuccess(false)
                    }
                }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                it.onError(Throwable("getNetworkState() has error."))
            }
        }
    }
}