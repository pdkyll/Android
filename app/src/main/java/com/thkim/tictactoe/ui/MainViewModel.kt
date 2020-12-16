package com.thkim.tictactoe.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thkim.tictactoe.R
import com.thkim.tictactoe.TableState
import com.thkim.tictactoe.util.LogT
import kotlin.random.Random

/*
 * Created by kth on 2020-12-13.
 */
class MainViewModel : ViewModel() {

    companion object {
        const val ROW = 3
        const val COL = 3
    }

    private val scoreArray by lazy { createMatrix(ROW, COL) }
    private val scoreState = HashMap<Int, TableState>()
    private val remainedArea = arrayListOf<Int>()

    private val _playerData = MutableLiveData<Array<Any>>()
    val playerData: LiveData<Array<Any>>
        get() = _playerData

    private val _computerData = MutableLiveData<Array<Any>>()
    val computerData: LiveData<Array<Any>>
        get() = _computerData

    private val _alertsDone = MutableLiveData<TableState>()
    val alertsDone: LiveData<TableState>
        get() = _alertsDone

    private val _resetAllImage = MutableLiveData<Int>()
    val resetAllImage: MutableLiveData<Int>
        get() = _resetAllImage


    init {
        initData()
    }


    fun checkTicTacToe(data: Int) {
        LogT.d("player data : $data")
        LogT.d("scoreState[data] : ${scoreState[data]}")

        when (scoreState[data]) {
            TableState.NONE -> {
                scoreState[data] = TableState.PLAYER

                _playerData.value = arrayOf(data, TableState.PLAYER)

                if (checkLine(TableState.PLAYER)) _alertsDone.value = TableState.PLAYER
                else _alertsDone.value = TableState.NONE

                removeData(data)
                for (i in 0 until remainedArea.size) LogT.d("remainedArea $i : ${remainedArea[i]}")
            }
            TableState.PLAYER -> {
                _playerData.value = arrayOf(data, TableState.ALREADY)
            }
            TableState.COMPUTER -> {
                _playerData.value = arrayOf(data, TableState.ALREADY)
            }
            else -> {

            }
        }

    }

    fun setComputerData() {
        LogT.start()
        var randomNum: Int

        while (true) {
            if (remainedArea.size <= 1) {
                _alertsDone.value = TableState.DONE
                break
            }
            randomNum = Random.nextInt(remainedArea.size)
            LogT.d("randomNum : $randomNum")
            LogT.d("remainedArea[randomNum] : ${remainedArea[randomNum]}")
            if (scoreState[remainedArea[randomNum]] == TableState.NONE) {

                scoreState[remainedArea[randomNum]] = TableState.COMPUTER

                _computerData.value = arrayOf(remainedArea[randomNum], TableState.COMPUTER)

                if (checkLine(TableState.COMPUTER)) _alertsDone.value = TableState.COMPUTER

                removeData(remainedArea[randomNum])
                break
            } else {
                continue
            }
        }

    }

    fun resetAllData() {
        remainedArea.removeAll(remainedArea)
        initData()
        _resetAllImage.value = R.drawable.ic_initial_image
    }

    private fun initData() {
        for (i in 0 until ROW) {
            for (j in 0 until COL) {
                val num = scoreArray[i][j]
                scoreState[num] = TableState.NONE
                remainedArea.add(num)
            }
        }
    }

    private fun checkLine(checker: TableState): Boolean {
        return checkCrossLtoR(checker) ||
                checkCrossRtoL(checker) ||
                checkColLine(checker) ||
                checkRowLine(checker)
    }

    private fun checkCrossLtoR(checker: TableState): Boolean {

        for (i in 0 until COL) {
            LogT.d("L To R $i : ${scoreState[scoreArray[i][i]]}")
            if (scoreState[scoreArray[i][i]] == checker) {
                continue
            } else {
                return false
            }
        }

        return true
    }

    private fun checkCrossRtoL(checker: TableState): Boolean {
        for (i in 0 until COL) {
            LogT.d("R TO L${scoreState[scoreArray[i][(COL - 1) - i]]}")
            if (scoreState[scoreArray[i][(COL - 1) - i]] == checker) {
                continue
            } else {
                return false
            }
        }
        return true
    }

    private fun checkColLine(checker: TableState): Boolean {
        var temp = 0
        for (row in 0 until ROW) {
            for (col in 0 until COL) {
                LogT.d("[$row, $col] : ${scoreState[scoreArray[row][col]]}")
                if (scoreState[scoreArray[row][col]] == checker) {
                    ++temp
                    if (temp == COL) return true
                    continue
                }
            }
            temp = 0
        }

        return false
    }

    private fun checkRowLine(checker: TableState): Boolean {
        var temp = 0
        for (row in 0 until ROW) {
            for (col in 0 until COL) {
                LogT.d("[$col, $row] : ${scoreState[scoreArray[col][row]]}")
                if (scoreState[scoreArray[col][row]] == checker) {
                    ++temp
                    if (temp == COL) return true
                    continue
                }
            }
            temp = 0
        }

        return false
    }

    private fun removeData(data: Int) {
        remainedArea.remove(data)
    }

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
}

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel() as T
    }

}
