package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.viewpager2.widget.ViewPager2;

import com.taehyungkim.project_a.R;
import com.taehyungkim.project_a.db.MovieList;
import com.taehyungkim.project_a.network.MovieAPIHelper;
import com.taehyungkim.project_a.network.MovieAPIInterface;
import com.taehyungkim.project_a.network.NetworkStatus;
import com.taehyungkim.project_a.ui.adapter.MovieListAdapter;
import com.taehyungkim.project_a.vm.MovieListViewModel;
import com.taehyungkim.project_a.vo.MovieListInfo;
import com.taehyungkim.project_a.vo.MovieListsResponse;

import java.util.ArrayList;
import java.util.List;

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

    private static int currentNum;
    private static String currentName;

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private ViewModelStore viewModelStore = new ViewModelStore();

    private MovieListViewModel viewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_list, container, false);

        viewPager2 = (ViewPager2) rootView.findViewById(R.id.viewPager);

        // 영화 상세보기 클릭 시 영화 이름과 ID를 넘겨주어
        // API에서 현재 ViewPager의 영화 정보를 호출할 수 있도록 한다.
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentNum = position;
                currentName = movie_name.get(currentNum);
            }
        });

        // ViewModelFactory 객체 선언
        if (viewModelFactory == null && getActivity() != null) {
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        }

        // viewModel 객체 선언
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MovieListViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        movieListAdapter = new MovieListAdapter(movie_view, movie_name, book_rate, age); // ViewPager에 적용할 Adapter 객체
        viewPager2.setAdapter(movieListAdapter);
        viewPager2.setOffscreenPageLimit(3); // 최대 3개의 스크린을 허용하도록 설정

        // ViewPager 양 옆의 화면 미리보기가 되도록 설정
        viewPager2.setPageTransformer((page, position) ->
                page.setTranslationX(position * -200.0f)
        );


    }

    @Override
    public void onResume() {
        super.onResume();

        // 네트워크 상태 체크 변수
        int status = NetworkStatus.getConnectivityStatus(context);

        if (status == NetworkStatus.TYPE_NOT_CONNECTED) { // 네트워크 연결 안됨
            // 네트워크가 연결되어 있지 않을 경우 데이터베이스에 저장된 데이터를 불러와 ViewPager에 호출한다.
            Toast.makeText(context, "네트워크가 연결되어 있지 않습니다.", Toast.LENGTH_LONG).show();

            movie_view.clear();
            movie_name.clear();
            book_rate.clear();
            age.clear();

            movieListAdapter.notifyDataSetChanged();

            viewModel.getAll().observe(this, new Observer<List<MovieList>>() {
                @Override
                public void onChanged(List<MovieList> movieLists) {
                    for (int i = 0; i < movieLists.size(); i++) {
                        movie_view.add(movieLists.get(i).movieImage);
                        movie_name.add(movieLists.get(i).movieName);
                        book_rate.add(movieLists.get(i).bookRate);
                        age.add(movieLists.get(i).movie_age);
                    }

                    movieListAdapter.notifyDataSetChanged();
                }
            });

        } else { // 모바일, 무선랜으로 네트워크 연결됨

            // 네트워크가 연결되어 있을 경우 API를 호출하여 ViewPager에 보여주도록 설정
            apiInterface = MovieAPIHelper.getClient().create(MovieAPIInterface.class);
            // 1: 예매율순, 2: 큐레이션, 3: 상영예정작
            Call<MovieListsResponse> call = apiInterface.getMovieList("1");
            call.enqueue(callback);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
        }
    }

    // 현재 ViewPager에서 보고있는 영화의 이름을 반환
    public String getCurrentName() {
        Log.d(getClass().toString(), "thkim test " + currentName);
        return currentName;
    }

    // 현재 ViewPager에서 보고있는 영화의 ID를 반환
    public int getCurrentNum() {
        return currentNum;
    }

    // API 호출 메서드
    private Callback<MovieListsResponse> callback = new Callback<MovieListsResponse>() {
        @Override
        public void onResponse(Call<MovieListsResponse> call, Response<MovieListsResponse> response) {
            if (response.body() != null) {
                ArrayList list = (ArrayList) response.body().getResult();

                viewModel.deleteAll(); // 기존 데이터베이스 목록 삭제

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

                    MovieList movieList = new MovieList(i, movieListInfo.image,
                            movieListInfo.title, movieListInfo.reservation_rate,
                            movieListInfo.grade);
                    viewModel.insert(movieList);
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
