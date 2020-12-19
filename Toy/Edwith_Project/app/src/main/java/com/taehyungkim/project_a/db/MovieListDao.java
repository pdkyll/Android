package com.taehyungkim.project_a.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Created by kth on 2020-06-22.
 */
@Dao
public interface MovieListDao {
    @Query("SELECT * FROM movie_list")
    LiveData<List<MovieList>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieList movieList);

    @Delete
    void delete(MovieList movieList);

    @Query("DELETE FROM movie_list")
    void deleteAll();
}
