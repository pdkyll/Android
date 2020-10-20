package com.thkim.fragmentdialogtest

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class LifecycleLogger(private var lifecycleOwner: LifecycleOwner) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny() {
        DLog.d("${lifecycleOwner.lifecycle.currentState} : #### ${lifecycleOwner.javaClass.simpleName} ####")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated(source: LifecycleOwner) {
        DLog.d("${lifecycleOwner.lifecycle.currentState} : #### ${lifecycleOwner.javaClass.simpleName} ####")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        DLog.d( "onStart : #### ${lifecycleOwner.javaClass.simpleName} ####")
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            DLog.d( "currentState is greater or equal to INITIALIZED")
        }
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            DLog.d( "currentState is greater or equal to CREATED")
        }
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            DLog.d( "currentState is greater or equal to STARTED")
        }
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            DLog.d( "currentState is greater or equal to RESUMED")
        }
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)) {
            DLog.d( "currentState is greater or equal to DESTROYED")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        DLog.d("${lifecycleOwner.lifecycle.currentState} : #### ${lifecycleOwner.javaClass.simpleName} ####")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        DLog.d("${lifecycleOwner.lifecycle.currentState} : #### ${lifecycleOwner.javaClass.simpleName} ####")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        DLog.d("${lifecycleOwner.lifecycle.currentState} : #### ${lifecycleOwner.javaClass.simpleName} ####")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        DLog.d("${lifecycleOwner.lifecycle.currentState} : #### ${lifecycleOwner.javaClass.simpleName} ####")
    }


}