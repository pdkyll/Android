package com.thkim.market.di

import com.thkim.market.api.SignInApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
 * Created by Thkim on 2020/10/11
 */
@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideSignInApi(): SignInApi = SignInApi()
}