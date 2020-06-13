package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.taehyungkim.project_a.DTO.MovieCreateCommentResponse;
import com.taehyungkim.project_a.R;
import com.taehyungkim.project_a.network.MovieAPIHelper;
import com.taehyungkim.project_a.network.MovieAPIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kth on 2020-06-12.
 */
public class MovieWritingCommentFragment extends Fragment {

    private Context context;
    private EditText contentsInput;
    private RatingBar ratingBar;
    private MovieAPIInterface apiInterface;

    private ViewGroup rootView;

    private MovieListFragment movieListFragment = new MovieListFragment();
    private String getMovieName;
    private int getMovieNum;

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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writing_comment, container, false);

        Button bt_save = (Button) rootView.findViewById(R.id.saveButton);
        Button bt_cancel = (Button) rootView.findViewById(R.id.cancelButton);

        getMovieName = movieListFragment.getCurrentName();
        getMovieNum = movieListFragment.getCurrentNum();

        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        contentsInput = (EditText) rootView.findViewById(R.id.contentsInput);

        apiInterface = MovieAPIHelper.getClient().create(MovieAPIInterface.class);


        bt_save.setOnClickListener(v -> {
            String comments = contentsInput.getText().toString();
            float rating = ratingBar.getRating();
            if (comments.length() > 0 && rating != 0) {
                Call<MovieCreateCommentResponse> call = apiInterface.
                        createComment(getMovieNum + 1, "Thkim", ratingBar.getRating(), comments);
                call.enqueue(callback);
            } else if (rating == 0) {
                Toast.makeText(context, "평점을 입력해 주세요.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "입력된 한줄평이 없습니다.", Toast.LENGTH_LONG).show();
            }
        });

        bt_cancel.setOnClickListener(v -> {
            Navigation.findNavController(rootView).navigate(R.id.action_nav_writing_comment_to_nav_movie_details);
        });

        return rootView;
    }

    private Callback<MovieCreateCommentResponse> callback = new Callback<MovieCreateCommentResponse>() {
        @Override
        public void onResponse(Call<MovieCreateCommentResponse> call, Response<MovieCreateCommentResponse> response) {
            if (response.body() != null && response.body().code == 200) {
                Toast.makeText(context, "한줄평이 작성되었습니다.", Toast.LENGTH_LONG).show();
                Navigation.findNavController(rootView).navigate(R.id.action_nav_writing_comment_to_nav_movie_details);
            } else {
                Toast.makeText(context, "한줄평이 작성 실패", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<MovieCreateCommentResponse> call, Throwable t) {

        }
    };
}
