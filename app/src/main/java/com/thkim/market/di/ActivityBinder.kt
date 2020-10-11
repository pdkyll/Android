package com.thkim.market.di

import com.thkim.market.di.ui.SignInModule
import com.thkim.market.ui.signin.SignInActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/*
 * Created by Thkim on 2020/10/11
 */
@Module
abstract class ActivityBinder {
    // SignInActivity 를 객체 그래프에 추가할 수 있도록 합니다.
    @ContributesAndroidInjector(modules = [SignInModule::class])
    abstract fun bindSignInActivity(): SignInActivity
}