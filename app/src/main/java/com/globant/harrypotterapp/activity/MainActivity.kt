package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.databinding.ActivityMainBinding
import com.globant.harrypotterapp.util.Constants.GRYFFINDOR
import com.globant.harrypotterapp.util.Constants.HUFFLEPUFF
import com.globant.harrypotterapp.util.Constants.RAVENCLAW
import com.globant.harrypotterapp.util.Constants.SLYTHERIN
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.MainData
import com.globant.harrypotterapp.viewmodel.MainStatus
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
        mainViewModel.getHousesLiveData().observe(::getLifecycle, ::updateUI)
        mainViewModel.fetchHouses()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchHouses()
    }

    private fun setOnClickListeners() {
        binding.mainActivitySpellsButton.setOnClickListener {
            mainViewModel.goToSpells()
        }
        binding.mainActivityGryffindorButton.setOnClickListener {
            mainViewModel.goToHouse(GRYFFINDOR)
        }
        binding.mainActivityHufflepuffButton.setOnClickListener {
            mainViewModel.goToHouse(HUFFLEPUFF)
        }
        binding.mainActivityRavenclawButton.setOnClickListener {
            mainViewModel.goToHouse(RAVENCLAW)
        }
        binding.mainActivitySlytherinButton.setOnClickListener {
            mainViewModel.goToHouse(SLYTHERIN)
        }
    }

    private fun updateUI(data: Event<MainData>) {
        val mainState = data.getContentIfNotHandled()
        when (mainState?.status) {
            MainStatus.LOADING_MAIN -> {
                showMessage(R.string.main_activity_loading_message)
            }
            MainStatus.SUCCESS_MAIN -> {
                showMessage(R.string.main_activity_success_message)
            }
            MainStatus.ERROR_MAIN -> {
                showMessage(R.string.main_activity_error_message)
            }
            MainStatus.GO_HOUSE -> {
                openHouseActivity(mainState.houseName)
            }
            MainStatus.GO_SPELLS -> {
                openSpellsActivity()
            }
        }
    }

    private fun showMessage(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    private fun openHouseActivity(houseName: String) {
        startActivity(HouseActivity.getIntent(this, houseName))
    }

    private fun openSpellsActivity() {
        startActivity(SpellsActivity.getIntent(this))
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
