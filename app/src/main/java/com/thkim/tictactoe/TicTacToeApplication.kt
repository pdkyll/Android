package com.thkim.tictactoe

import android.app.Application
import com.thkim.tictactoe.util.PreferenceUtil

/*
 * Created by kth on 2020-12-16.
 */
class TicTacToeApplication : Application() {
    companion object {
        lateinit var pref: PreferenceUtil
    }

    override fun onCreate() {
        pref = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}