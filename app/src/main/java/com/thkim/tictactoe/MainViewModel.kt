package com.thkim.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.random.Random

/*
 * Created by kth on 2020-12-13.
 */
class MainViewModel : ViewModel() {

    private val scoreArray by lazy { createMatrix(3, 3) }
    private val scoreState = HashMap<Int, TableState>()
    private val remainedArea = arrayListOf<Int>()

    private val _playerData = MutableLiveData<Array<Any>>()
    val playerData: LiveData<Array<Any>>
        get() = _playerData

    private val _computerData = MutableLiveData<Array<Any>>()
    val computerData: LiveData<Array<Any>>
        get() = _computerData

    init {
        for (i in 0..2) {
            for (j in 0..2) {
                val num = scoreArray[i][j]
                scoreState[num] = TableState.NONE
                remainedArea.add(num)
            }
        }
    }


    fun checkTicTacToe(data: Int) {
        LogT.d("player data : $data")

        when (scoreState[data]) {
            TableState.NONE -> {
                scoreState[data] = TableState.PLAYER
                _playerData.value = arrayOf(data, TableState.PLAYER)
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
        var randomNum: Int

        while (true) {
            if (remainedArea.size <= 1) {
                _computerData.value = arrayOf(remainedArea.last(), TableState.DONE)
                break
            }
            randomNum = Random.nextInt(remainedArea.size)
            LogT.d("randomNum : $randomNum")
            LogT.d("remainedArea[randomNum] : ${remainedArea[randomNum]}")
            if (scoreState[remainedArea[randomNum]] == TableState.NONE) {
                scoreState[remainedArea[randomNum]] = TableState.COMPUTER
                _computerData.value = arrayOf(remainedArea[randomNum], TableState.COMPUTER)
                removeData(remainedArea[randomNum])
                break
            } else {
                continue
            }
        }

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
