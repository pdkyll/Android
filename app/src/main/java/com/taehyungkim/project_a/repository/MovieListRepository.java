package com.taehyungkim.project_a.repository;

import android.app.Application;
import android.graphics.Movie;

import androidx.lifecycle.LiveData;

import com.taehyungkim.project_a.db.MovieList;
import com.taehyungkim.project_a.db.MovieListDao;
import com.taehyungkim.project_a.db.MovieListDatabase;

import java.util.List;

/**
 * Created by kth on 2020-06-22.
 */
public class MovieListRepository {
    private Application application;

    private MovieListDatabase movieListDatabase;
    private MovieListDao movieListDao;
    private LiveData<List<MovieList>> movieList;

    public MovieListRepository(Application application) {
        this.application = application;
        movieListDatabase = MovieListDatabase.getDatabase(application);
        movieListDao = movieListDatabase.movieListDao();
        movieList = movieListDao.getAll();
    }

    public LiveData<List<MovieList>> getAll() {
        return movieList;
    }

    // Database의 컨트롤은 메인스레드에서 동작할 경우 Runtime Exception이 발생할 수 있으므로
    // Thread를 통해 백그라운드에서 동작하도록 해야한다.
    public void insert(MovieList movieList) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    movieListDao.insert(movieList);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(MovieList movieList) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    movieListDao.delete(movieList);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    movieListDao.deleteAll();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
