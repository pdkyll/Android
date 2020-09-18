package com.thkim.kotlinprogramming.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.Composable
import androidx.fragment.app.Fragment
import androidx.ui.tooling.preview.Preview
import org.jetbrains.anko.*

/*
 * Created by kth on 2020-09-18.
 */
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null

        // Thkim on 2020.09.18
        // create 부분에서 에러가 발생하는데 이유는 모르겠다.
        // 중요한건 아니니까 일단은 넘어갔다.
//        return MainFragmentUI().createView(AnkoContext.create(context, this))
    }
}


@Composable
class MainFragmentUI : AnkoComponent<MainFragment> {
    override fun createView(ui: AnkoContext<MainFragment>) = ui.apply {
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