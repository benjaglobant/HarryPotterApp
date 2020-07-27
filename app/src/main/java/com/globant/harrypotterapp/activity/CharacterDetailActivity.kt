package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.globant.domain.entity.CharacterDetail
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.databinding.ActivityCharacterDetailBinding
import com.globant.harrypotterapp.viewmodel.CharacterDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding
    private val characterDetailViewModel by viewModel<CharacterDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        characterDetailViewModel.getCharacterDetailLiveData().observe(::getLifecycle, ::updateUI)
        characterDetailViewModel.fetchCharacterDetail()
    }

    private fun updateUI(data: CharacterDetail) {
        binding.characterDetailActivityName.text = data.name
        binding.characterDetailActivityRole.text = getString(R.string.character_detail_activity_role_text, data.role)
        binding.characterDetailActivityHouse.text = getString(R.string.character_detail_activity_house_text, data.house)
        binding.characterDetailActivityMinistryOfMagic.text =
            getString(R.string.character_detail_activity_ministry_of_magic_text, getStringValueForDetail(data.ministryOfMagic))
        binding.characterDetailActivityOrderOfThePhoenix.text =
            getString(R.string.character_detail_activity_order_of_the_phoenix_text, getStringValueForDetail(data.orderOfThePhoenix))
        binding.characterDetailActivityDumbledoresArmy.text =
            getString(R.string.character_detail_activity_dumbledores_army_text, getStringValueForDetail(data.dumbledoresArmy))
        binding.characterDetailActivityDeathEater.text =
            getString(R.string.character_detail_activity_death_eater_text, getStringValueForDetail(data.deathEater))
        binding.characterDetailActivityBloodStatus.text =
            getString(R.string.character_detail_activity_blood_status_text, data.bloodStatus)
        binding.characterDetailActivitySpecies.text =
            getString(R.string.character_detail_activity_species_text, data.species)
    }

    private fun getStringValueForDetail(detail: Boolean): String {
        return if (detail) {
            YES
        } else {
            NO
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, CharacterDetailActivity::class.java)
        private const val NO = "No"
        private const val YES = "Yes"
    }
}
