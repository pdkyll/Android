package com.thkim.kotlinprogramming.kotlin

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

/*
 * Created by Thkim on 2020/09/16
 */
class AnkoLogTest : AnkoLogger {
    override val loggerTag: String
        get() = "Thkim_TAG"

    private fun doSomething() {
        info("doSomething() called")
    }

    private fun doSomethingWithParameter(number: Int) {
        // Log.DEBUG 레벨로 로그 메시지를 기록합니다.
        // String 타입이 아닌 인자는 해당 인자의 toString() 함수 반환값을 기록합니다.
        debug(number)
    }
}