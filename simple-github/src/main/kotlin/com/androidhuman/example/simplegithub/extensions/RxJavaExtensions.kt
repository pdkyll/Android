package com.androidhuman.example.simplegithub.extensions

import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import io.reactivex.disposables.Disposable

/*
 * Created by kth on 2020-09-20.
 * CompositeDisposable 에 디스포저블을 추가하기 위해
 * CompositeDisposable.add() 함수를 사용했습니다.
 * add() 함수를 호출하는 대신 += 연산자를 사용하여
 * CompositeDisposable 객체에 디스포저블 객체를 추가하도록 한다면
 * 더 직관적이면서도 편리하게 코드를 작성할 수 있습니다.
 */
// CompositeDisposable 의 '+=' 연산자 뒤에 Disposable 타입이 오는 경우를 재정의합니다.
operator fun AutoClearedDisposable.plusAssign(disposable: Disposable) {

    // CompositeDisposable.add() 함수를 호출합니다.
    this.add(disposable)
}