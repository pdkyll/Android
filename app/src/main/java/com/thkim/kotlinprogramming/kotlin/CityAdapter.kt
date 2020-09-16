package com.thkim.kotlinprogramming.kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thkim.kotlinprogramming.R
import kotlinx.android.synthetic.main.item_city.view.*

/*
 * Created by Thkim on 2020/09/16
 */
class CityAdapter : RecyclerView.Adapter<CityAdapter.Holder>() {

    private val cities = listOf(
        "Seoul" to "SEO",
        "Tokyo" to "TOK",
        "Mountain View" to "MTV",
        "Singapore" to "SIN",
        "New York" to "NYC"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.Holder =
        Holder(parent)

    override fun onBindViewHolder(holder: CityAdapter.Holder, position: Int) {
        val (city, code) = cities[position]

        // 코틀린 안드로이드 익스텐션을 사용하여 레이아웃 내 뷰에 접근하려면
        // 뷰홀더 내의 itemView 를 거쳐야한다.
        with(holder.itemView) {
            // 뷰 ID 를 사용하여 인스턴스에 바로 접근한다.
            tv_city_name.text = city
            tv_city_code.text = code
        }


    }

    override fun getItemCount() = cities.size

    inner class Holder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
    )
}