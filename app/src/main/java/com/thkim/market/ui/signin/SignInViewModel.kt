package com.thkim.market.ui.signin

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.thkim.market.api.SignInApi
import com.thkim.market.util.SupportOptional
import com.thkim.market.util.optionalOf
import com.thkim.util.DLog
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

/*
 * Created by Thkim on 2020/10/10
 */
class SignInViewModel(
    private val signInApi: SignInApi
) : ViewModel() {

    // Firebase 에서 가져온 유저값을 전달할 서브젝트.
    val currentUser: BehaviorSubject<SupportOptional<FirebaseUser>> = BehaviorSubject.create()

    // 에러 메시지를 전달할 서브젝트.
    val message: PublishSubject<String> = PublishSubject.create()

    // 작업 진행 상태를 전달할 서브젝트입니다. 초깃값으로 false 를 지정합니다.
    val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    val isConnected: PublishSubject<Boolean> = PublishSubject.create()

    fun requestSignInAccount(idToken: String, auth: FirebaseAuth): Disposable =
        signInApi.getAuthCredential(idToken)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                DLog.start()
                isLoading.onNext(true)
                DLog.end()
            }
            .doOnTerminate {
                DLog.start()
                isLoading.onNext(false)
                DLog.end()
            }
            .subscribe({ credential ->
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            DLog.d("task is successful.")
                            currentUser.onNext(optionalOf(auth.currentUser))
                        }
                    }
            }) {
                message.onNext(it.message ?: "Unexpected error.")
            }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getNetworkState(context: Context): Disposable =
        signInApi.getNetworkState(context)
            .subscribeOn(Schedulers.computation())
            .subscribe(Consumer {
                DLog.start()
                isConnected.onNext(it)
                DLog.end()
            })
}