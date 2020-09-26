package com.androidhuman.example.simplegithub.api.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repositories")
class GithubRepo(
        val name: String,

        @SerializedName("full_name")
        // fullName 프로퍼티를 주요 키로 사용하며,
        // 테이블 내 필드 이름은 full_name 으로 지정합니다.
        @PrimaryKey @ColumnInfo(name = "full_name") val fullName: String,

        // GithubOwner 형태의 객체를 표현합니다.
        // GithubOwner 내 필드를 테이블에 함께 저장합니다.
        @Embedded val owner: GithubOwner,

        // 널 값을 허용할 수 있는 타입으로 선언합니다.
        val description: String?,
        val language: String?,
        @SerializedName("updated_at")

        // updatedAt 프로퍼티의 테이블 내 필드 이름을 updated_at 으로 지정합니다.
        @ColumnInfo(name = "updated_at") val updatedAt: String,

        @SerializedName("stargazer_count") val stars: Int
)