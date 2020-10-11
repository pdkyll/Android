package com.thkim.market.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thkim.market.api.SignInApi

/*
 * Created by Thkim on 2020/10/10
 */
class SignInViewModelFactory(
    private val signInApi: SignInApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SignInViewModel(signInApi) as T
    }
}