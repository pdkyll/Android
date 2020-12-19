package com.thkim.fragmentdialogtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

/*
 * Created by kth on 2020-10-20.
 */
class EventDialogFragment : DialogFragment(), View.OnClickListener {
    companion object {
        private const val TAG = "EventDialogFragment"

        fun getInstance(): EventDialogFragment {
            val e: EventDialogFragment = EventDialogFragment()
            return e
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(LifecycleLogger(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_dialog, container)
        val btn = v.findViewById<Button>(R.id.btn_confirm)
        return v
    }

    override fun onClick(v: View?) {


    }
}