package com.taehyungkim.project_a.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by kth on 2020-06-22.
 */
@Entity(tableName = "movie_list")
public class MovieList {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "movie_image")
    public String movieImage;
    @ColumnInfo(name = "movie_name")
    public String movieName;
    @ColumnInfo(name = "book_rate")
    public float bookRate;
    @ColumnInfo(name = "movie_age")
    public int movie_age;

    public MovieList(int id, String movieImage, String movieName, float bookRate, int movie_age) {
        this.id = id;
        this.movieImage = movieImage;
        this.movieName = movieName;
        this.bookRate = bookRate;
        this.movie_age = movie_age;
    }

    public int getId() {
        return id;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public String getMovieName() {
        return movieName;
    }

    public float getBookRate() {
        return bookRate;
    }

    public int getMovie_age() {
        return movie_age;
    }
}
