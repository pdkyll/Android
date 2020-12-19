package com.androidhuman.example.simplegithub.data

import android.content.Context
import android.preference.PreferenceManager

class AuthTokenProvider(private val context: Context) {

    // SharedPreferences 에 액세스 토큰을 저장합니다.
    fun updateToken(token: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_AUTH_TOKEN, token)
                .apply()
    }

    // SharedPreferences 에 저장되어 있는 액세스 토큰을 반환합니다.
    // 저장되어 있는 액세스 토큰이 없는 경우 널 값을 반환합니다.
    val token: String?
        get() = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_AUTH_TOKEN, null)

    // 정적 필드는 동반 객체 내부의 프로퍼티로 변환됩니다.
    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

}