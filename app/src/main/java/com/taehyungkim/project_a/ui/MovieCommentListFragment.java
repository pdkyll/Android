package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taehyungkim.project_a.DTO.MovieCommentList;
import com.taehyungkim.project_a.DTO.MovieCommentResponse;
import com.taehyungkim.project_a.R;
import com.taehyungkim.project_a.network.MovieAPIHelper;
import com.taehyungkim.project_a.network.MovieAPIInterface;
import com.taehyungkim.project_a.ui.adapter.MovieDetailsAdapter;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kth on 2020-06-12.
 */
public class MovieCommentListFragment extends Fragment {

    public RecyclerView writingRecyclerView;
    public MovieDetailsAdapter commentAdapter;

    public ArrayList<String> id = new ArrayList<>();
    public ArrayList<String> time = new ArrayList<>();
    public ArrayList<Float> rating = new ArrayList<>();
    public ArrayList<String> comment = new ArrayList<>();
    public ArrayList<Integer> recommendCount = new ArrayList<>();

    private TextView peopleCount;
    private TextView ratingCount;

    private Context context;

    private int getMovieNum;
    private MovieListFragment movieListFragment = new MovieListFragment();

    private MovieAPIInterface apiInterface;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_comment_list, container, false);

        getMovieNum = movieListFragment.getCurrentNum();

        writingRecyclerView = (RecyclerView) rootView.findViewById(R.id.comment_recycler_view);
        writingRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // 리사이클러 뷰에 구분선 라인 추가
        writingRecyclerView.addItemDecoration(new DividerItemDecoration(context, 1));

        apiInterface = MovieAPIHelper.getClient().create(MovieAPIInterface.class);
        Call<MovieCommentResponse> call = apiInterface.getMovieComment(getMovieNum + 1);
        call.enqueue(callback);

        commentAdapter = new MovieDetailsAdapter(id, time, rating, comment, recommendCount);
        writingRecyclerView.setAdapter(commentAdapter);

        peopleCount = (TextView) rootView.findViewById(R.id.peopleCount);
        ratingCount = (TextView) rootView.findViewById(R.id.ratingCount);

        Button bt_writing = (Button) rootView.findViewById(R.id.writingBt_view);
        bt_writing.setOnClickListener(v -> {
            // temp
            Toast.makeText(context, "작성하기 버튼 클릭", Toast.LENGTH_LONG).show();
        });

        return rootView;
    }

    private Callback<MovieCommentResponse> callback = new Callback<MovieCommentResponse>() {

        @Override
        public void onResponse(Call<MovieCommentResponse> call, Response<MovieCommentResponse> response) {
            if (response.body() != null) {
                ArrayList list = (ArrayList) response.body().getResult();

                id.clear();
                time.clear();
                rating.clear();
                comment.clear();
                recommendCount.clear();
                for (int i = 0; i < list.size(); i++) {
                    MovieCommentList movieCommentList = (MovieCommentList) list.get(i);
                    id.add(movieCommentList.review_id);
                    time.add(movieCommentList.time);
                    rating.add(movieCommentList.rating);
                    comment.add(movieCommentList.contents);
                    recommendCount.add(movieCommentList.recommend);
                }

                commentAdapter.notifyDataSetChanged();
                peopleCount.setText(String.format(Locale.US, "(%s명 참여)", list.size()));
            }
        }

        @Override
        public void onFailure(Call<MovieCommentResponse> call, Throwable t) {
            Log.d("Thkim failure", "retrofit fail");
        }
    };
}
