package com.taehyungkim.project_a.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.taehyungkim.project_a.db.MovieList;
import com.taehyungkim.project_a.repository.MovieListRepository;

import java.util.List;

/**
 * Created by kth on 2020-06-22.
 */
public class MovieListViewModel extends AndroidViewModel {
    Application application;
    private MovieListRepository movieListRepository;
    private LiveData<List<MovieList>> movieList;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        movieListRepository = new MovieListRepository(application);
        movieList = movieListRepository.getAll();
    }

    public LiveData<List<MovieList>> getAll() {
        return movieList;
    }

    public void insert(MovieList movieList) {
        movieListRepository.insert(movieList);
    }

    public void delete(MovieList movieList) {
        movieListRepository.delete(movieList);
    }

    public void deleteAll() {
        movieListRepository.deleteAll();
    }
}
