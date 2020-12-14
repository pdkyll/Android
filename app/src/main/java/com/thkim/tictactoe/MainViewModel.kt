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

    private var score = mutableMapOf<Int, String>()
    private var player = mutableListOf<Int>()
    private var computer = mutableListOf<Int>()

    private val scoreArray by lazy { createMatrix(3, 3) }
    private val hashTable = HashMap<Int, TableState>()

    private val _playerData = MutableLiveData<TableState>()
    val playerData: LiveData<TableState>
        get() = _playerData

    init {
        for (i in 0..2) {
            for (j in 0..2) {
                val num = scoreArray[i][j]
                hashTable[num] = TableState.NONE
            }
        }
    }


    fun checkTicTacToe(data: Int) {

        when (hashTable[data]) {
            TableState.NONE -> {
                hashTable[data] = TableState.PLAYER
                _playerData.postValue(TableState.PLAYER)
            }
            TableState.PLAYER -> {

            }
            TableState.COMPUTER -> {

            }
        }

        setComputerData()
    }

    private fun setComputerData() {

        while (true) {
            val randomNum = Random.nextInt(0, 9)
            if (score[randomNum] == "none") {
                score[randomNum] = "computer"
                break
            } else {
                continue
            }
        }

        _playerData.postValue(TableState.COMPUTER)
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
