package com.thkim.kotlinprogramming.kotlin

import org.jetbrains.anko.*

// Use DSL(DomainSpecific Language)
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