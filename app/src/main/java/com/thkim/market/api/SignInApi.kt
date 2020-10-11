package com.thkim.market.api

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.rxjava3.core.Observable

/*
 * Created by Thkim on 2020/10/11
 */
class SignInApi {

    fun getAuthCredential(idToken: String): Observable<AuthCredential> = Observable.create {
        if (!it.isDisposed) {
            it.onNext(GoogleAuthProvider.getCredential(idToken, null))
            it.onComplete()
        } else {
            it.onError(Throwable("SignInApi has error."))
        }
    }
}