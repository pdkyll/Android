package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taehyungkim.project_a.DTO.MovieCommentList;
import com.taehyungkim.project_a.DTO.MovieCommentResponse;
import com.taehyungkim.project_a.DTO.MovieDetailsInfo;
import com.taehyungkim.project_a.DTO.MovieDetailsResponse;
import com.taehyungkim.project_a.R;
import com.taehyungkim.project_a.network.MovieAPIHelper;
import com.taehyungkim.project_a.network.MovieAPIInterface;
import com.taehyungkim.project_a.ui.adapter.MovieDetailsAdapter;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends Fragment {
    private ImageView thumbUpImg;
    private ImageView thumbDownImg;

    private Button writingBt;
    private Button seeAllBt;

    private boolean isLikeBtClicked = false;
    private boolean isDislikeBtClicked = false;

    public RecyclerView com_recycler_view;
    public MovieDetailsAdapter commentAdapter;

    private ViewGroup rootView;

    public ArrayList<String> id = new ArrayList<>();
    public ArrayList<String> time = new ArrayList<>();
    public ArrayList<Float> rating = new ArrayList<>();
    public ArrayList<String> comment = new ArrayList<>();
    public ArrayList<Integer> recommendCount = new ArrayList<>();

    private Context context;

    private RatingBar ratingBar_view;

    private ImageView thumb_image;
    private TextView movie_title;
    private ImageView movie_grade;
    private TextView movie_date;
    private TextView movie_genre_duration;
    private TextView movie_like;
    private TextView movie_dislike;
    private TextView movie_reservation_rating;
    private TextView movie_rating;
    private TextView movie_audience;
    private TextView movie_synopsis;
    private TextView movie_dir;
    private TextView movie_act;

    private MovieAPIInterface apiInterface;

    private String getMovieName;
    private int getMovieNum;

    private MovieListFragment movieListFragment = new MovieListFragment();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (context != null) {
            context = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);

        getMovieName = movieListFragment.getCurrentName();
        getMovieNum = movieListFragment.getCurrentNum();


        thumbUpImg = (ImageView) rootView.findViewById(R.id.like_img_view);
        thumbDownImg = (ImageView) rootView.findViewById(R.id.dislike_img_view);

        writingBt = (Button) rootView.findViewById(R.id.writingBt_view);
        seeAllBt = (Button) rootView.findViewById(R.id.seeAllBt_view);

        thumb_image = (ImageView) rootView.findViewById(R.id.details_thumb_image);
        movie_title = (TextView) rootView.findViewById(R.id.details_movie_title);
        movie_grade = (ImageView) rootView.findViewById(R.id.details_grade_image);
        movie_date = (TextView) rootView.findViewById(R.id.details_date);
        movie_genre_duration = (TextView) rootView.findViewById(R.id.details_genre_duration);
        movie_like = (TextView) rootView.findViewById(R.id.details_like);
        movie_dislike = (TextView) rootView.findViewById(R.id.details_dislike);
        movie_reservation_rating = (TextView) rootView.findViewById(R.id.details_reservation_rating);
        movie_rating = (TextView) rootView.findViewById(R.id.details_aud_rating);
        movie_audience = (TextView) rootView.findViewById(R.id.details_audience);
        movie_synopsis = (TextView) rootView.findViewById(R.id.details_synopsis);
        movie_dir = (TextView) rootView.findViewById(R.id.details_director);
        movie_act = (TextView) rootView.findViewById(R.id.details_actor);

        ratingBar_view = (RatingBar) rootView.findViewById(R.id.ratingBar_view);


        apiInterface = MovieAPIHelper.getClient().create(MovieAPIInterface.class);
        Call<MovieDetailsResponse> call = apiInterface.getMovieDetails(getMovieName);
        call.enqueue(callback);

        apiInterface = MovieAPIHelper.getClient().create(MovieAPIInterface.class);
        Call<MovieCommentResponse> call2 = apiInterface.getMovieComment(getMovieNum + 1);
        call2.enqueue(callback2);

        com_recycler_view = (RecyclerView) rootView.findViewById(R.id.comment_recycler_view);
        com_recycler_view.setLayoutManager(new LinearLayoutManager(context));

        // 리사이클러 뷰에 구분선 라인 추가
        com_recycler_view.addItemDecoration(new DividerItemDecoration(context, 1));

        commentAdapter = new MovieDetailsAdapter(id, time, rating, comment, recommendCount);
        com_recycler_view.setAdapter(commentAdapter);

        setListener();

        return rootView;
    }

    private Callback<MovieDetailsResponse> callback = new Callback<MovieDetailsResponse>() {

        @Override
        public void onResponse(Call<MovieDetailsResponse> call, Response<MovieDetailsResponse> response) {
            if (response.body() != null) {
                ArrayList list = (ArrayList) response.body().getResult();

                MovieDetailsInfo movieDetailsInfo = (MovieDetailsInfo) list.get(0);

                Glide.with(rootView).load(movieDetailsInfo.thumb).placeholder(new ColorDrawable(Color.BLACK))
                        .override(thumb_image.getWidth(), thumb_image.getHeight()).into(thumb_image);
                movie_title.setText(movieDetailsInfo.title);
                if (movieDetailsInfo.grade == 12) {
                    movie_grade.setImageResource(R.drawable.ic_12);
                } else if (movieDetailsInfo.grade == 15) {
                    movie_grade.setImageResource(R.drawable.ic_15);
                } else if (movieDetailsInfo.grade == 19) {
                    movie_grade.setImageResource(R.drawable.ic_19);
                } else {
                    movie_grade.setImageResource(R.drawable.ic_all);
                }

                movie_date.setText(String.format(Locale.US, "%s 개봉",
                        movieDetailsInfo.date.replace('-', '.')));
                movie_genre_duration.setText(String.format(Locale.US, "%s / %d 분",
                        movieDetailsInfo.genre, movieDetailsInfo.duration));
                movie_like.setText(String.format(Locale.US, "%d", movieDetailsInfo.like));
                movie_dislike.setText(String.format(Locale.US, "%d", movieDetailsInfo.dislike));
                movie_reservation_rating.setText(String.format(Locale.US, "%d위 %.2f%%", getMovieNum + 1, movieDetailsInfo.reservation_rate));
                movie_rating.setText(String.format(Locale.US, "%.1f", movieDetailsInfo.reviewer_rating));
                ratingBar_view.setRating(movieDetailsInfo.reviewer_rating / 2);
                movie_audience.setText(String.format(Locale.US, "%,d", movieDetailsInfo.audience));
                movie_synopsis.setText(String.format(Locale.US, "%s", movieDetailsInfo.synopsis));
                movie_dir.setText(String.format(Locale.US, "%s", movieDetailsInfo.director));
                movie_act.setText(String.format(Locale.US, "%s", movieDetailsInfo.actor));

            }
        }

        @Override
        public void onFailure(Call<MovieDetailsResponse> call, Throwable t) {
            Log.d("Thkim failure", "retrofit fail");
        }
    };

    private Callback<MovieCommentResponse> callback2 = new Callback<MovieCommentResponse>() {
        @Override
        public void onResponse(Call<MovieCommentResponse> call, Response<MovieCommentResponse> response) {
            if (response.body() != null) {
                ArrayList list = (ArrayList) response.body().getResult();

                id.clear();
                time.clear();
                rating.clear();
                comment.clear();
                recommendCount.clear();
                if (list.size() > 1) {
                    for (int i = 0; i < 2; i++) {
                        MovieCommentList movieCommentList = (MovieCommentList) list.get(i);
                        id.add(movieCommentList.review_id);
                        time.add(movieCommentList.time);
                        rating.add(movieCommentList.rating);
                        comment.add(movieCommentList.contents);
                        recommendCount.add(movieCommentList.recommend);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<MovieCommentResponse> call, Throwable t) {

        }
    };

    public void setListener() {
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.like_img_view:
                        if (!isLikeBtClicked && !isDislikeBtClicked) { // not clicked both "like" and "dislike"
                            thumbUpImg.setImageResource(R.drawable.ic_thumb_up_selected);
                            movie_like.setText(String.format(Locale.US, "%d", Integer.parseInt(movie_like.getText().toString()) + 1));
                            isLikeBtClicked = true;
                            break;
                        } else if (isLikeBtClicked && !isDislikeBtClicked) { // clicked "like" and not clicked "dislike"
                            break;
                        } else { // clicked "dislike" and not clicked "like"
                            thumbUpImg.setImageResource(R.drawable.ic_thumb_up_selected);
                            thumbDownImg.setImageResource(R.drawable.ic_thumb_down);

                            isLikeBtClicked = true;
                            isDislikeBtClicked = false;

                            movie_like.setText(String.format(Locale.US, "%d", Integer.parseInt(movie_like.getText().toString()) + 1));

                            movie_dislike.setText(String.format(Locale.US, "%d", Integer.parseInt(movie_dislike.getText().toString()) - 1));
                        }
                        break;

                    case R.id.dislike_img_view:
                        if (!isLikeBtClicked && !isDislikeBtClicked) { // not clicked both "like" and "dislike"
                            thumbDownImg.setImageResource(R.drawable.ic_thumb_down_selected);
                            movie_dislike.setText(String.format(Locale.US, "%d", Integer.parseInt(movie_dislike.getText().toString()) + 1));
                            isDislikeBtClicked = true;
                            break;
                        } else if (isDislikeBtClicked && !isLikeBtClicked) { // clicked "dislike" and not clicked "like"
                            break;
                        } else { // clicked "like" and not clicked "dislike"
                            thumbDownImg.setImageResource(R.drawable.ic_thumb_down_selected);
                            thumbUpImg.setImageResource(R.drawable.ic_thumb_up);

                            isDislikeBtClicked = true;
                            isLikeBtClicked = false;

                            movie_dislike.setText(String.format(Locale.US, "%d", Integer.parseInt(movie_dislike.getText().toString()) + 1));

                            movie_like.setText(String.format(Locale.US, "%d", Integer.parseInt(movie_like.getText().toString()) - 1));
                        }
                        break;

                    case R.id.writingBt_view:
//                        Intent wiringCommentIntent = new Intent(context, WritingCommentActivity.class);
//                        startActivity(wiringCommentIntent);
                        Navigation.findNavController(rootView).navigate(R.id.action_nav_movie_details_to_nav_writing_comment);
                        break;

                    case R.id.seeAllBt_view:
//                        Intent commentListIntent = new Intent(context, CommentListActivity.class);
//                        startActivity(commentListIntent);
                        Navigation.findNavController(rootView).navigate(R.id.action_nav_movie_details_to_nav_comment_list);
                        break;
                }
            }
        };

        thumbUpImg.setOnClickListener(listener);
        thumbDownImg.setOnClickListener(listener);
        writingBt.setOnClickListener(listener);
        seeAllBt.setOnClickListener(listener);
    }
}
