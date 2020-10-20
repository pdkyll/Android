package com.thkim.fragmentdialogtest

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi

/*
 * Created by kth on 2020-10-20.
 */
class DialogActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_dialog)
        findViewById<TextView>(R.id.btn1)
            .setOnClickListener {
                finish()
            }
    }

    override fun onStart() {
        super.onStart()
        DLog.d("#### onStart() ####")
    }

    override fun onResume() {
        super.onResume()
        DLog.d("#### onResume() ####")
    }

    override fun onPause() {
        super.onPause()
        DLog.d("#### onPause() ####")
    }

    override fun onStop() {
        super.onStop()
        DLog.d("#### onStop() ####")
    }

    override fun onRestart() {
        super.onRestart()
        DLog.d("#### onRestart() ####")
    }

    override fun onDestroy() {
        super.onDestroy()
        DLog.d("#### onDestroy() ####")
    }
}