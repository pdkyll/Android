package com.thkim.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.thkim.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val DATA = 0
        const val STATE = 1
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels { MainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                    Snackbar.make(binding.layoutParent, "It's already.", Snackbar.LENGTH_SHORT)
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
                    Snackbar.make(binding.layoutParent, "YOU ARE WINNER.", Snackbar.LENGTH_SHORT)
                        .show()
                }
                TableState.COMPUTER -> {
                    LogT.d("COMPUTER is Winner.")
                    Snackbar.make(binding.layoutParent, "YOU LOSE.", Snackbar.LENGTH_SHORT)
                        .show()
                }
                TableState.DONE -> {
                    LogT.d("Game is Done.")
                    Snackbar.make(binding.layoutParent, "Draw Game.", Snackbar.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    mainViewModel.setComputerData()
                }
            }
        })
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
        }
    }

    private fun setPlayerImage(data: Int, resource: Int) {
        when (data) {
            0 -> binding.ivTableItem0.setImageResource(resource)
            1 -> binding.ivTableItem1.setImageResource(resource)
            2 -> binding.ivTableItem2.setImageResource(resource)
            3 -> binding.ivTableItem3.setImageResource(resource)
            4 -> binding.ivTableItem4.setImageResource(resource)
            5 -> binding.ivTableItem5.setImageResource(resource)
            6 -> binding.ivTableItem6.setImageResource(resource)
            7 -> binding.ivTableItem7.setImageResource(resource)
            8 -> binding.ivTableItem8.setImageResource(resource)
        }
    }
}