package com.thkim.market

import com.thkim.market.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/*
 * Created by Thkim on 2020/10/11
 */
class MarketApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

}