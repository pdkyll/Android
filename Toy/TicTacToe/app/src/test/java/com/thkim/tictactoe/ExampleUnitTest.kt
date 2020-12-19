package com.thkim.tictactoe

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private fun createMatrix(row: Int, col: Int): Array<Array<Int>> {

        var temp = -col

        return Array(row) {
            Array(col) { i: Int ->
                if (i == 0) {
                    temp += col
                }
                i + temp
            }
        }
    }

    @Test
    fun addition_isCorrect() {
        println(Arrays.deepToString(createMatrix(3, 3)))

        assertEquals(4, 2 + 2)
    }

}