package com.thkim.kotlinprogramming.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.thkim.kotlinprogramming.R
import org.jetbrains.anko.*

class MainActivityUI : AnkoComponent<MainActivity> {

    override fun createView(ui: AnkoContext<MainActivity>) = ui.apply {
        verticalLayout {
            padding = dip(12)

            textView("Enter Login Credentials")

            editText {
                hint = "E-mail"
            }

            editText {
                hint = "Password"
            }

            button("Submit")

        }
    }.view

}