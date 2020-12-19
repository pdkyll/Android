package com.thkim.roomdbtest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Created by kth on 2020-12-06.
 *
 * This class will describe the Entity (which represents the SQLite table) for your words.
 *
 * @Entity(tableName = "word_table") : Each @Entity class represents a SQLite table.
 *
 * @PrimaryKey : Every entity needs a primary key.
 *
 * @ColumnInfo(name = "word") : Specifies the name of the column in the table if you want it to be different from the name of the member variable.
 */
@Entity(tableName = "word_table")
class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)
