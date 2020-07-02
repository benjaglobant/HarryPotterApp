package com.globant.harrypotterapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globant.harrypotterapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.mainActivitySpellsButton.setOnClickListener {
            startActivity(SpellsActivity.getIntent(this))
        }
    }

    companion object {
        fun getIntent(activity: Activity): Intent = Intent(activity, MainActivity::class.java)
    }
}
