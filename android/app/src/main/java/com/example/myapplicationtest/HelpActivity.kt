package com.example.myapplicationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Help"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
