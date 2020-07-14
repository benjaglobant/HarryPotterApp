package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globant.harrypotterapp.databinding.ActivityMainBinding
import com.globant.harrypotterapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
        mainViewModel.fetchHouses()
        // TODO: observe and react to this Job, and use de fetched data
    }

    private fun setOnClickListeners() {
        binding.mainActivitySpellsButton.setOnClickListener {
            startActivity(SpellsActivity.getIntent(this))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
