package com.taehyungkim.project_a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        // 구성변경이 생긴 후에 안드로이드는 이전에 추가된 프래그먼트를 자동으로 추가한다.
        // 따라서 자동으로 다시 시작된 경우가 아닐 때만 우리가 추가해야 한다.
        if (savedInstanceState == null) {
            FragmentTransaction ft = fm.beginTransaction();

//            mEarthquakeListFragment = new EarthquakeListFragment();
//            ft.add(R.id.main_activity_frame, mEarthquakeListFragment, TAG_LIST_FRAGMENT);
//            ft.commitNow();
        } else {
//            mEarthquakeListFragment = (EarthquakeListFragment)fm.findFragmentByTag(TAG_LIST_FRAGMENT);
        }
    }
}
