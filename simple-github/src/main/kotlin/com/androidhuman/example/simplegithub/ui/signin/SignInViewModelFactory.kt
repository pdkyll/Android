package com.androidhuman.example.simplegithub.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidhuman.example.simplegithub.api.AuthApi
import com.androidhuman.example.simplegithub.data.AuthTokenProvider

/*
 * Created by Thkim on 2020/09/27
 */
class SignInViewModelFactory(
        val api: AuthApi,
        val authTokenProvider: AuthTokenProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SignInViewModel(api, authTokenProvider) as T
    }

}