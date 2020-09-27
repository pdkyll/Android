package com.androidhuman.example.simplegithub.ui.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidhuman.example.simplegithub.api.GithubApi

/*
 * Created by Thkim on 2020/09/27
 */
class RepositoryViewModelFactory(val api: GithubApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RepositoryViewModel(api) as T
    }

}