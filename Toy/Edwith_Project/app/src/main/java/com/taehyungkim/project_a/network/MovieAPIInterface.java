package com.taehyungkim.project_a.network;

import com.taehyungkim.project_a.vo.MovieCommentResponse;
import com.taehyungkim.project_a.vo.MovieCreateCommentResponse;
import com.taehyungkim.project_a.vo.MovieDetailsResponse;
import com.taehyungkim.project_a.vo.MovieListsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kth on 2020-06-10.
 */
public interface MovieAPIInterface {
    @GET("/movie/readMovieList")
    Call<MovieListsResponse> getMovieList(@Query("type") String type);

    @GET("/movie/readMovie")
    Call<MovieDetailsResponse> getMovieDetails(@Query("name") String name);

    @GET("/movie/readCommentList")
    Call<MovieCommentResponse> getMovieComment(@Query("id") int id);

    @GET("/movie/createComment")
    Call<MovieCreateCommentResponse> createComment(@Query("id") int id,
                                                   @Query("writer") String writer,
                                                   @Query("rating") float rating,
                                                   @Query("contents") String contents);
}
