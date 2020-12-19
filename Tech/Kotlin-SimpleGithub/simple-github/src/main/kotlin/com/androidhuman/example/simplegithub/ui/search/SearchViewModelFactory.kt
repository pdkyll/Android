package com.androidhuman.example.simplegithub.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidhuman.example.simplegithub.api.GithubApi
import com.androidhuman.example.simplegithub.data.SearchHistoryDao

/*
 * Created by Thkim on 2020/09/27
 */
class SearchViewModelFactory(
        val api: GithubApi,
        val searchHistoryDao: SearchHistoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(api, searchHistoryDao) as T
    }

}