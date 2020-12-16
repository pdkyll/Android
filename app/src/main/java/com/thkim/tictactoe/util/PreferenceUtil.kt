package com.thkim.tictactoe.util

import android.content.Context
import android.content.SharedPreferences

/*
 * Created by kth on 2020-12-16.
 */
class PreferenceUtil(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences("score_pref", Context.MODE_PRIVATE)

    fun setInitPlayer() = pref.edit().putBoolean("initialUser", false).apply()

    fun isInitPlayer() = pref.getBoolean("initialUser", true)

    fun setMyScore(myScore: Int) = pref.edit().putInt("myScore", myScore).apply()

    fun getMyScore(): Int = pref.getInt("myScore", 0)

    fun setComScore(comScore: Int) = pref.edit().putInt("comScore", comScore).apply()

    fun getComScore(): Int = pref.getInt("comScore", 0)
}