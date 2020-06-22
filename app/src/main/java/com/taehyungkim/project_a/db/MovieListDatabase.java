package com.taehyungkim.project_a.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by kth on 2020-06-22.
 */
@Database(entities = {MovieList.class}, version = 1, exportSchema = false)
public abstract class MovieListDatabase extends RoomDatabase {
    public abstract MovieListDao movieListDao();

    public static MovieListDatabase INSTANCE;

    public static MovieListDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, MovieListDatabase.class, "movie_list")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
