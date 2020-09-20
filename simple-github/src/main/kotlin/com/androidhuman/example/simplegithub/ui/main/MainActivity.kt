package com.androidhuman.example.simplegithub.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnActivityMainSearch.setOnClickListener { // 저장소 검색 액티비티를 호출합니다.
            // 호출할 액티비티만 명시합니다.
            startActivity<SearchActivity>()
        }
    }
}