package com.thkim.market.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thkim.market.R
import com.thkim.market.util.DLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        DLog.end()
    }
}