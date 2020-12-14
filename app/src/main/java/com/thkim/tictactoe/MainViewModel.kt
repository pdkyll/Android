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

    private val _playerData = MutableLiveData<String>()
    val playerData: LiveData<String>
        get() = _playerData

    init {
        for (i in 0..9) {
            score[i] = "none"
        }
    }


    fun checkTicTacToe(data: Int) {

        when (score[data]) {
            "none" -> {
                score[data] = "player"
                _playerData.postValue("player")
            }
            "player" -> {

            }
            "computer" -> {

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

        _playerData.postValue("computer")
    }
}

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel() as T
    }

}
