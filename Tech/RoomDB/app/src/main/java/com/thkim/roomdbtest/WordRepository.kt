package com.thkim.roomdbtest

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/*
 * Created by kth on 2020-12-06.
 *
 * The DAO is passed into the repository constructor as opposed to the whole database.
 * This is because it only needs access to the DAO, since the DAO contains all the read/write methods for the database.
 * There's no need to expose the entire database to the repository.
 *
 * The list of words is a public property. It's initialized by getting the Flow list of words from Room;
 * we can do this because of how we defined the getAlphabetizedWords method to return Flow in the "Observing database changes" step.
 * Room executes all queries on a separate thread.
 *
 * The suspend modifier tells the compiler that this needs to be called from a coroutine or another suspending function.
 *
 * Room executes suspend queries off the main thread.
 */
// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}