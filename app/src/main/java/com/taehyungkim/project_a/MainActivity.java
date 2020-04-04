package com.taehyungkim.project_a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Movie;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_LIST_FRAGMENT = "TAG_LIST_FRAGMENT";

    ContentListFragment mContentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        // 구성변경이 생긴 후에 안드로이드는 이전에 추가된 프래그먼트를 자동으로 추가한다.
        // 따라서 자동으로 다시 시작된 경우가 아닐 때만 우리가 추가해야 한다.
        if (savedInstanceState == null) {
            FragmentTransaction ft = fm.beginTransaction();

            mContentListFragment = new ContentListFragment();
            ft.add(R.id.content_frame, mContentListFragment, TAG_LIST_FRAGMENT);
            ft.commitNow();
        } else {
            mContentListFragment = (ContentListFragment)fm.findFragmentByTag(TAG_LIST_FRAGMENT);
        }


        List<MovieContent> dummyQuakes = new ArrayList<MovieContent>(0);
        dummyQuakes.add(new MovieContent(getString(R.string.content_title), getString(R.string.content)));
        dummyQuakes.add(new MovieContent(getString(R.string.director_title), getString(R.string.director)));
        dummyQuakes.add(new MovieContent(getString(R.string.evaluation_title), getDrawable(R.drawable.ic_review_selected),
                                            getString(R.string.writing_title), getString(R.string.see_all_writing_content_bt)));
        dummyQuakes.add(new MovieContent(getDrawable(R.drawable.ic_facebook), getDrawable(R.drawable.ic_kakao),
                                        getString(R.string.reservation_bt)));

        mContentListFragment.setMovieContents(dummyQuakes);
    }
}
