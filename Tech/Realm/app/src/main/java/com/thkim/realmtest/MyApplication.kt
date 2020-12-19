package com.thkim.realmtest

import android.app.Application
import io.realm.Realm

/*
 * Created by Thkim on 12/5/20
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);

        // In this example, no default configuration is set,
        // so by default, `RealmConfiguration.Builder().build()` is used.
    }
}