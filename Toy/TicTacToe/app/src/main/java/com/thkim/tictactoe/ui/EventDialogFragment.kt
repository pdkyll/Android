package com.thkim.tictactoe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.thkim.tictactoe.databinding.FragmentEventDialogBinding

/*
 * Created by kth on 2020-12-17.
 */
class EventDialogFragment : DialogFragment() {

    private val eventBinding by lazy { FragmentEventDialogBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        return eventBinding.root
    }
}