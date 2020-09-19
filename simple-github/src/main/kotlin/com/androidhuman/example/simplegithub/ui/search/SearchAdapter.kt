package com.androidhuman.example.simplegithub.ui.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.androidhuman.example.simplegithub.R
import com.androidhuman.example.simplegithub.api.model.GithubRepo
import com.androidhuman.example.simplegithub.ui.GlideApp
import java.util.*
import kotlinx.android.synthetic.main.item_repository.*
import kotlinx.android.synthetic.main.item_repository.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.RepositoryHolder>() {

    // 초깃값을 빈 MutableList 로 할당합니다.
    private var items: MutableList<GithubRepo> = mutableListOf()
    private val placeholder = ColorDrawable(Color.GRAY)
    private var listener: ItemClickListener? = null

    // 항상 RepositoryHolder 객체만 반환하므로 단일 표현식으로 표현할 수 있다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RepositoryHolder(parent)

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {

        // item.get(position) 대신 배열 인덱스 접근 연산자를 사용합니다.
        // let() 함수를 사용하여 값이 사용되는 범위를 한정합니다.
        items[position].let { repo ->
            // with() 함수를 사용하여 holder.itemView 를 여러 번 호출하지 않도록 합니다.
            with(holder.itemView) {
                GlideApp.with(context)
                        .load(repo.owner.avatarUrl)
                        .placeholder(placeholder)
                        // 뷰 ID 를 사용하여 뷰 인스턴스에 접근합니다.
                        .into(ivItemRepositoryProfile)

                // 뷰 ID 를 사용하여 뷰 인스턴스에 접근합니다.
                tvItemRepositoryName.text = repo.fullName
                tvItemRepositoryLanguage.text =
                        if (TextUtils.isEmpty(repo.language))
                            context.getText(R.string.no_language_specified)
                        else
                            repo.language

                setOnClickListener {
                    if (null != listener) {
                        listener!!.onItemClick(repo)
                    }
                }
            }
        }
    }

    // 항상 리스트 크기만을 반환하므로 이 함수 또한 단일 표현식으로 표현할 수 있다.
    override fun getItemCount() = items.size

    fun setItems(items: List<GithubRepo>) {
        // 인자로 받은 리스트의 형태를 어댑터 내부에서 사용하는
        // 리스트 형태(내부 자료 변경이 가능한 형태)로 변환해 주어야 합니다.
        this.items = items.toMutableList()
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    fun clearItems() {
        items.clear()
    }

    class RepositoryHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false))

    interface ItemClickListener {
        fun onItemClick(repository: GithubRepo)
    }
}