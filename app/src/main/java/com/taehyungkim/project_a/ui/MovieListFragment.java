package com.taehyungkim.project_a.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.taehyungkim.project_a.R;
import com.taehyungkim.project_a.ui.adapter.MovieListAdapter;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    private ArrayList<Integer> movie_view = new ArrayList<>();
    private ArrayList<String> movie_name = new ArrayList<>();
    private ArrayList<String> book_rate = new ArrayList<>();
    private ArrayList<String> age = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();

    ViewPager2 viewPager2;
    MovieListAdapter movieListAdapter;

    Context context;

    Button bt_movie_details;


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

        movie_view.add(R.drawable.image1);
        movie_name.add("1. 군 도");
        book_rate.add("61.6%");
        age.add("15세 관람가");
        date.add("D-1");

        movie_view.add(R.drawable.image2);
        movie_name.add("2. 공 조");
        book_rate.add("61.6%");
        age.add("15세 관람가");
        date.add("D-1");

        movie_view.add(R.drawable.image3);
        movie_name.add("3. 더 킹");
        book_rate.add("61.6%");
        age.add("15세 관람가");
        date.add("D-1");

        movie_view.add(R.drawable.image4);
        movie_name.add("4. 레지던트 이블");
        book_rate.add("61.6%");
        age.add("15세 관람가");
        date.add("D-1");

        movie_view.add(R.drawable.image5);
        movie_name.add("5. 럭키");
        book_rate.add("61.6%");
        age.add("15세 관람가");
        date.add("D-1");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieListAdapter = new MovieListAdapter(movie_view, movie_name, book_rate, age, date);
        viewPager2.setAdapter(movieListAdapter);
        viewPager2.setOffscreenPageLimit(3);

        viewPager2.setPageTransformer((page, position) -> {
            page.setTranslationX(position * -200.0f);
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
