package com.androidhuman.example.simplegithub.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {
    internal lateinit var btnSearch: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch = findViewById(R.id.btnActivityMainSearch)
        btnSearch.setOnClickListener { // 저장소 검색 액티비티를 호출합니다.
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }
}