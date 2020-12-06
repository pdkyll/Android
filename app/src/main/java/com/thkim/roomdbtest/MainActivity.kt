package com.thkim.roomdbtest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * MainActivity: displays words in a list using a RecyclerView and the WordListAdapter.
 * In MainActivity, there is an Observer that observes the words from the database and is notified when they change.
 *
 * NewWordActivity: adds a new word to the list.
 *
 * WordViewModel: provides methods for accessing the data layer, and it returns LiveData so that MainActivity can set up the observer relationship.*
 *
 * LiveData<List<Word>>: Makes possible the automatic updates in the UI components. We can convert from Flow, to LiveData by calling flow.toLiveData().
 *
 * Repository: manages one or more data sources. The Repository exposes methods for the ViewModel to interact with the underlying data provider. In this app, that backend is a Room database.
 *
 * Room: is a wrapper around and implements a SQLite database. Room does a lot of work for you that you used to have to do yourself.
 *
 * DAO: maps method calls to database queries, so that when the Repository calls a method such as getAlphabetizedWords(), Room can execute SELECT * FROM word_table ORDER BY word ASC.
 *
 * DAO can expose suspend queries for one-shot requests and Flow queries - when we want to get notified of changes in the database.
 *
 * Word: is the entity class that contains a single word.
 *
 * Views and Activities (and Fragments) only interact with the data through the ViewModel. As such, it doesn't matter where the data comes from.
 */
class MainActivity : AppCompatActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordViewModel.allWords.observe(this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val word = Word(it)
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}