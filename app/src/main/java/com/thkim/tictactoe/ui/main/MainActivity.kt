package com.thkim.tictactoe.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.thkim.tictactoe.*
import com.thkim.tictactoe.databinding.ActivityMainBinding
import com.thkim.tictactoe.ui.EventDialogFragment
import com.thkim.tictactoe.util.LogT

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val DATA = 0
        const val STATE = 1
    }

    private val mainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels { MainViewModelFactory() }

    private val _myScore = MutableLiveData<Int>()
    val myScore: MutableLiveData<Int>
        get() = _myScore

    private val _comScore = MutableLiveData<Int>()
    val comScore: MutableLiveData<Int>
        get() = _comScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        mainBinding.apply {
            lifecycleOwner = this@MainActivity
            this.mainViewModel = this@MainActivity.mainViewModel
            this.mainActivity = this@MainActivity
        }

        with(TicTacToeApplication.pref) {
            if (isInitPlayer()) setInitPlayer()

            _myScore.value = this.getMyScore()
            _comScore.value = this.getComScore()
        }


        mainViewModel.playerData.observe(this, {
            LogT.start()
            when (it[STATE]) {
                TableState.NONE -> {

                }
                TableState.PLAYER -> {
                    LogT.d("PLAYER")
                    setPlayerImage(it[DATA] as Int, R.drawable.ic_player_chess)
                }
                TableState.COMPUTER -> {

                }
                TableState.ALREADY -> {
                    Snackbar.make(mainBinding.layoutParent, "It's already.", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        mainViewModel.computerData.observe(this, {
            when (it[STATE]) {
                TableState.COMPUTER -> {
                    Handler(Looper.getMainLooper()).apply {
                        postDelayed({
                            setPlayerImage(it[DATA] as Int, R.drawable.ic_computer_chess)
                        }, 600)
                    }
                }
            }
        })

        mainViewModel.alertsDone.observe(this, {
            LogT.start()
            when (it) {
                TableState.PLAYER -> {
                    LogT.d("PLAYER is Winner.")
                    TicTacToeApplication.pref.setMyScore(TicTacToeApplication.pref.getMyScore() + 1)

                    _myScore.value = TicTacToeApplication.pref.getMyScore()

                    Snackbar.make(mainBinding.layoutParent, "YOU ARE WINNER.", Snackbar.LENGTH_SHORT)
                        .show()
                    EventDialogFragment().show(supportFragmentManager, "TAG_EventDialogFragment")
                }
                TableState.COMPUTER -> {
                    LogT.d("COMPUTER is Winner.")
                    TicTacToeApplication.pref.setComScore(TicTacToeApplication.pref.getComScore() + 1)

                    _comScore.value = TicTacToeApplication.pref.getComScore()

                    Snackbar.make(mainBinding.layoutParent, "YOU LOSE.", Snackbar.LENGTH_SHORT)
                        .show()
                    EventDialogFragment().show(supportFragmentManager, "TAG_EventDialogFragment")
                }
                TableState.DONE -> {
                    LogT.d("Game is Done.")
                    Snackbar.make(mainBinding.layoutParent, "Draw Game.", Snackbar.LENGTH_SHORT)
                        .show()
                    EventDialogFragment().show(supportFragmentManager, "TAG_EventDialogFragment")
                }
                else -> {
                    mainViewModel.setComputerData()
                }
            }

            EventDialogFragment().show(supportFragmentManager, "TAG_EventDialogFragment")
        })

        mainViewModel.resetAllData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_table_item_0 -> {
                mainViewModel.checkTicTacToe(0)
            }
            R.id.iv_table_item_1 -> {
                mainViewModel.checkTicTacToe(1)
            }
            R.id.iv_table_item_2 -> {
                mainViewModel.checkTicTacToe(2)
            }
            R.id.iv_table_item_3 -> {
                mainViewModel.checkTicTacToe(3)
            }
            R.id.iv_table_item_4 -> {
                mainViewModel.checkTicTacToe(4)
            }
            R.id.iv_table_item_5 -> {
                mainViewModel.checkTicTacToe(5)
            }
            R.id.iv_table_item_6 -> {
                mainViewModel.checkTicTacToe(6)
            }
            R.id.iv_table_item_7 -> {
                mainViewModel.checkTicTacToe(7)
            }
            R.id.iv_table_item_8 -> {
                mainViewModel.checkTicTacToe(8)
            }
            R.id.bt_main_reset -> {
                mainViewModel.resetAllData()
            }
        }
    }

    private fun setPlayerImage(data: Int, resource: Int) {
        when (data) {
            0 -> mainBinding.ivTableItem0.setImageResource(resource)
            1 -> mainBinding.ivTableItem1.setImageResource(resource)
            2 -> mainBinding.ivTableItem2.setImageResource(resource)
            3 -> mainBinding.ivTableItem3.setImageResource(resource)
            4 -> mainBinding.ivTableItem4.setImageResource(resource)
            5 -> mainBinding.ivTableItem5.setImageResource(resource)
            6 -> mainBinding.ivTableItem6.setImageResource(resource)
            7 -> mainBinding.ivTableItem7.setImageResource(resource)
            8 -> mainBinding.ivTableItem8.setImageResource(resource)
        }
    }
}