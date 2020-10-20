package com.thkim.fragmentdialogtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(LifecycleLogger(this))

        val e = EventDialogFragment.getInstance()

        findViewById<Button>(R.id.btnFragment)
            .setOnClickListener { e.show(supportFragmentManager, "EventDialogFragment") }

        findViewById<Button>(R.id.btnActivity)
            .setOnClickListener { startActivity(Intent(this, DialogActivity::class.java)) }
    }
}