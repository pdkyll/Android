package com.thkim.tictactoe

import android.widget.ImageView
import androidx.databinding.BindingAdapter

/*
 * Created by kth on 2020-12-16.
 */
object BindingAdapter {

    @JvmStatic
    @BindingAdapter("resetImage")
    fun setResetImage(imageView: ImageView, resId: Int) {
        imageView.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("playerImage")
    fun setPlayerImage(imageView: ImageView, resId: Int) {
        imageView.setImageResource(resId)
    }
}