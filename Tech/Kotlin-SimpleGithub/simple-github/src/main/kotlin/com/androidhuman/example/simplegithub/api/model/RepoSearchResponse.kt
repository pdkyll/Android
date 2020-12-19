package com.androidhuman.example.simplegithub.api.model

import com.google.gson.annotations.SerializedName

class RepoSearchResponse(
        @field:SerializedName("total_count")
        val totalCount: Int,

        // GithubRepo 형태의 리스트를 표현합니다.
        val items: List<GithubRepo>
)