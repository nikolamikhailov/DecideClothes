package ru.l4gunner4l.decideclothes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, HolderFragment())
                .commit()
    }

    override fun onBackPressed() {

    }
}