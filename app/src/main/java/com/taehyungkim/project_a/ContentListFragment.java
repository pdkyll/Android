package com.taehyungkim.project_a;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContentListFragment extends Fragment {
    private ArrayList<MovieContent> mMovieContents = new ArrayList<MovieContent>();

    public ContentListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView mRecyclerView;
    private MovieContentRecyclerViewAdapter mMovieAdapter = new MovieContentRecyclerViewAdapter(mMovieContents);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moviecontent_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 리사이클러 뷰 어댑터 설정
        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    public void setMovieContents(List<MovieContent> movieContents) {
        for(MovieContent movieContent : movieContents) {
            if(!mMovieContents.contains(movieContent)) {
                mMovieContents.add(movieContent);
                mMovieAdapter.notifyItemInserted(mMovieContents.indexOf(movieContent));
            }
        }
    }
}
