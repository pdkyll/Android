package com.thkim.market.rx

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.thkim.util.Logger
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/*
 * Created by Thkim on 2020/10/10
 */
class AutoClearedDisposable(
    private val lifecycleOwner: AppCompatActivity,
    private val alwaysClearOnStop: Boolean = true,
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : LifecycleObserver {

    fun add(disposable: Disposable) {
        /*
         * LifecycleOwner.lifecycle 을 사용하여
         * 참조하고 있는 컴포넌트의 Lifecycle 객체에 접근합니다.
         * Lifecycle.currentState 를 사용하여 상태 정보인 Lifecycle.State 에 접근합니다.
         * Lifecycle.State.isAtLeast() 함수를 사용하여
         * 현재 상태가 특정 상태의 이후 상태인지 여부를 반환합니다.
         * 코틀린 표준 라이브러리에서 제공하는 check() 함수로
         * Lifecycle.State.isAtLeast() 함수의 반환 값이 참인지 확인하며,
         * 만약 참이 아닌 경우 IllegalStateException 예외를 발생시킵니다.
         */
        Logger.d(lifecycleOwner.lifecycle.currentState.toString())
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))

        compositeDisposable.add(disposable)
    }

    // onStop() 콜백 함수가 호출되면 cleanUp() 함수를 호출합니다.
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun cleanUp() {
        // onStop() 콜백 함수가 호출되었을 때 무조건 디스포저블을 해제하지 않는 경우,
        // 액티비티의 isFinishing() 메서드를 사용하여 액티비티가
        // 종료되지 않는 시점(예: 다른 액티비티 호출)에만 디스포저블을 해제하지 않도록 합니다.
        Logger.start()
        if (!alwaysClearOnStop && !lifecycleOwner.isFinishing) {
            return
        }

        // 관리하는 디스포저블을 해제합니다.
        compositeDisposable.clear()
        Logger.end()
    }

    // onDestroy() 콜백 함수가 호출되면 detachSelf() 함수를 호출합니다.
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        Logger.start()
        // 관리하는 디스포저블을 해제합니다.
        compositeDisposable.clear()

        // 더 이상 액티비티의 생명주기 이벤트를 받지 않도록 액티비티 생명주기 옵저버에서 제거합니다.
        lifecycleOwner.lifecycle.removeObserver(this)
        Logger.end()
    }
}