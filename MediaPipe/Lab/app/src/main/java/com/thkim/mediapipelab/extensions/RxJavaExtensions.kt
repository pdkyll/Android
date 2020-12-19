package com.thkim.mediapipelab.extensions

import com.thkim.mediapipelab.rx.AutoClearedDisposable
import io.reactivex.disposables.Disposable

/*
 * Created by Thkim on 2020/09/22
 */
operator fun AutoClearedDisposable.plusAssign(disposable: Disposable) {

    this.add(disposable)
}