package com.androidhuman.example.simplegithub.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidhuman.example.simplegithub.data.SearchHistoryDao

/*
 * Created by Thkim on 2020/09/27
 */
class MainViewModelFactory(val searchHistoryDao: SearchHistoryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(searchHistoryDao) as T
    }
}