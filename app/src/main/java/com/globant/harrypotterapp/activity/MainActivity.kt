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
        binding.mainActivityGryffindorButton.setOnClickListener {
            startActivity(HouseActivity.getIntent(this).putExtra(HOUSE_NAME, GRYFFINDOR))
        }
        binding.mainActivityHufflepuffButton.setOnClickListener {
            startActivity(HouseActivity.getIntent(this).putExtra(HOUSE_NAME, HUFFLEPUFF))
        }
        binding.mainActivityRavenclawButton.setOnClickListener {
            startActivity(HouseActivity.getIntent(this).putExtra(HOUSE_NAME, RAVENCLAW))
        }
        binding.mainActivitySlytherinButton.setOnClickListener {
            startActivity(HouseActivity.getIntent(this).putExtra(HOUSE_NAME, SLYTHERIN))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
        private const val GRYFFINDOR = "Gryffindor"
        private const val RAVENCLAW = "Ravenclaw"
        private const val SLYTHERIN = "Slytherin"
        private const val HUFFLEPUFF = "Hufflepuff"
        private const val HOUSE_NAME = "HouseName"
    }
}
