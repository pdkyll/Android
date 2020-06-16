package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.taehyungkim.project_a.vo.MovieListInfo;
import com.taehyungkim.project_a.vo.MovieListsResponse;
import com.taehyungkim.project_a.R;
import com.taehyungkim.project_a.network.MovieAPIHelper;
import com.taehyungkim.project_a.network.MovieAPIInterface;
import com.taehyungkim.project_a.ui.adapter.MovieListAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListFragment extends Fragment {

    private ArrayList<String> movie_view = new ArrayList<>();
    private ArrayList<String> movie_name = new ArrayList<>();
    private ArrayList<Float> book_rate = new ArrayList<>();
    private ArrayList<Integer> age = new ArrayList<>();
//    private ArrayList<String> date = new ArrayList<>();

    private ViewPager2 viewPager2;
    private MovieListAdapter movieListAdapter;

    private Context context;

    private MovieAPIInterface apiInterface;

    static int currentNum;
    static String currentName;


    @Override

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_list, container, false);

        viewPager2 = (ViewPager2) rootView.findViewById(R.id.viewPager);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentNum = position;
                currentName = movie_name.get(currentNum);
            }
        });

        apiInterface = MovieAPIHelper.getClient().create(MovieAPIInterface.class);
        // 1: 예매율순, 2: 큐레이션, 3: 상영예정작
        Call<MovieListsResponse> call = apiInterface.getMovieList("1");
        call.enqueue(callback);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieListAdapter = new MovieListAdapter(movie_view, movie_name, book_rate, age);
        viewPager2.setAdapter(movieListAdapter);
        viewPager2.setOffscreenPageLimit(3);

        viewPager2.setPageTransformer((page, position) ->
                page.setTranslationX(position * -200.0f)
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
        }
    }

    public String getCurrentName() {
        Log.d(getClass().toString(), "thkim test " + currentName);
        return currentName;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    private Callback<MovieListsResponse> callback = new Callback<MovieListsResponse>() {

        @Override
        public void onResponse(Call<MovieListsResponse> call, Response<MovieListsResponse> response) {
            if (response.body() != null) {
                ArrayList list = (ArrayList) response.body().getResult();

                movie_view.clear();
                movie_name.clear();
                book_rate.clear();
                age.clear();

                for (int i = 0; i < list.size(); i++) {
                    MovieListInfo movieListInfo = (MovieListInfo) list.get(i);
                    movie_view.add(movieListInfo.image);
                    movie_name.add(movieListInfo.title);
                    book_rate.add(movieListInfo.reservation_rate);
                    age.add(movieListInfo.grade);
                }
                movieListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<MovieListsResponse> call, Throwable t) {
            Log.d("thkim failure", "retrofit fail");
        }
    };
}
