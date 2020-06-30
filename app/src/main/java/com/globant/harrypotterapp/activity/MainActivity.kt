package com.globant.harrypotterapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globant.harrypotterapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    // TODO: implement SpellActivity

    companion object {
        fun getIntent(activity: Activity): Intent = Intent(activity, MainActivity::class.java)
    }
}
