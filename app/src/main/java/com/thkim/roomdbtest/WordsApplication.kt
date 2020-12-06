package com.thkim.roomdbtest

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/*
 * Created by kth on 2020-12-06.
 *
 * We want to only have one instance of the database and of the repository in our application.
 * An easy way to achieve this is by creating them as members of the Application class.
 * Like this, whenever they're needed, they will just be retrieved from the Application, rather than constructed every time.
 */
class WordsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}