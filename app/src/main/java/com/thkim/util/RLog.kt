package com.thkim.util

import android.util.Log
import com.thkim.market.BuildConfig

/*
 * Created by kth on 2020-10-16.
 */
class RLog {
    companion object {
        const val TAG = "Thkim"

        fun v(msg: String) {
            if (!BuildConfig.DEBUG_MODE) {
                Log.v(TAG, buildLogMsg(msg))
            }
        }

        fun d(msg: String) {
            if (!BuildConfig.DEBUG_MODE) {
                Log.d(TAG, buildLogMsg(msg))
            }
        }

        fun i(msg: String) {
            if (!BuildConfig.DEBUG_MODE) {
                Log.i(TAG, buildLogMsg(msg))
            }
        }

        fun w(msg: String) {
            if (!BuildConfig.DEBUG_MODE) {
                Log.w(TAG, buildLogMsg(msg))
            }
        }

        fun e(msg: String) {
            if (!BuildConfig.DEBUG_MODE) {
                Log.w(TAG, buildLogMsg(msg))
            }
        }

        fun start() {
            if (!BuildConfig.DEBUG_MODE) {
                Log.d(TAG, buildLogMsg("---> S"))
            }
        }

        fun end() {
            if (!BuildConfig.DEBUG_MODE) {
                Log.d(TAG, buildLogMsg("E <---"))
            }
        }


        private fun buildLogMsg(message: String): String {
            val ste = Thread.currentThread().stackTrace[4]

            return StringBuilder().append("[ ")
                .append(ste.fileName.replace(".java", ""))
                .append("::")
                .append(ste.methodName)
                .append(" ] ")
                .append(message)
                .toString()
        }
    }
}