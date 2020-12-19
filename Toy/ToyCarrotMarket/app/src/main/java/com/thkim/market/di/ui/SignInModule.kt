package com.thkim.market.di.ui

import com.thkim.market.api.SignInApi
import com.thkim.market.ui.signin.SignInViewModelFactory
import dagger.Module
import dagger.Provides

/*
 * Created by Thkim on 2020/10/11
 */
@Module
class SignInModule {
    @Provides
    fun provideViewModelFactory(signInApi: SignInApi): SignInViewModelFactory =
        SignInViewModelFactory(signInApi)
}