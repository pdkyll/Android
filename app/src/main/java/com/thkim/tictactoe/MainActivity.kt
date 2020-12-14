package com.thkim.tictactoe

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.thkim.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_table_item_1 -> {
                mainViewModel.checkTicTacToe(0)
                binding.ivTableItem1.setImageResource(R.drawable.ic_baseline_clear_24)
            }
            R.id.iv_table_item_2 -> {

            }
            R.id.iv_table_item_3 -> {

            }
            R.id.iv_table_item_4 -> {

            }
            R.id.iv_table_item_5 -> {

            }
            R.id.iv_table_item_6 -> {

            }
            R.id.iv_table_item_7 -> {

            }
            R.id.iv_table_item_8 -> {

            }
            R.id.iv_table_item_9 -> {

            }
        }
    }
}