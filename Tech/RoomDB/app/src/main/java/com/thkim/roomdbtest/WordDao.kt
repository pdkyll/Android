package com.thkim.roomdbtest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
 * Created by kth on 2020-12-06.
 *
 * WordDao is an interface; DAOs must either be interfaces or abstract classes.
 *
 * The @Dao annotation identifies it as a DAO class for Room.
 *
 * suspend fun insert(word: Word) : Declares a suspend function to insert one word.
 *
 * The @Insert annotation is a special DAO method annotation where you don't have to provide any SQL!
 * (There are also @Delete and @Update annotations for deleting and updating rows, but you are not using them in this app.)
 *
 * onConflict = OnConflictStrategy.IGNORE: The selected onConflict strategy ignores a new word if it's exactly the same as one already in the list.
 * To know more about the available conflict strategies, check out the documentation(https://developer.android.com/reference/androidx/room/OnConflictStrategy.html).
 *
 * suspend fun deleteAll(): Declares a suspend function to delete all the words.
 *
 * There is no convenience annotation for deleting multiple entities, so it's annotated with the generic @Query.
 *
 * @Query("DELETE FROM word_table"): @Query requires that you provide a SQL query as a string parameter to the annotation, allowing for complex read queries and other operations.
 *
 * fun getAlphabetizedWords(): List<Word>: A method to get all the words and have it return a List of Words.
 *
 * @Query("SELECT * FROM word_table ORDER BY word ASC"): Query that returns a list of words sorted in ascending order.
 */
@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}