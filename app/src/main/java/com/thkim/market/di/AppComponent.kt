package com.thkim.market.di

import android.app.Application
import com.thkim.market.MarketApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/*
 * Created by Thkim on 2020/10/11
 */
@Singleton
@Component(
    modules = [
        ApiModule::class,
        AndroidSupportInjectionModule::class, ActivityBinder::class
    ]
)
interface AppComponent : AndroidInjector<MarketApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        // 빌더 클래스는 컴포넌트를 반환하는 build() 함수를 반드시 포함해야 합니다.
        fun build(): AppComponent
    }
}