package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.globant.domain.entity.CharacterDetail
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.databinding.ActivityCharacterDetailBinding
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.CharacterDetailData
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus
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

    private fun updateUI(data: Event<CharacterDetailData<CharacterDetail>>) {
        val characterDetail = data.getContentIfNotHandled()
        when(characterDetail?.status){
            CharacterDetailStatus.SHOW_CHARACTER_DETAILS -> { showCharacterDetails(characterDetail.data) }
        }
    }

    private fun showCharacterDetails(details: CharacterDetail?){
        with(binding) {
            details?.let{
                characterDetailActivityToolbar.title = it.name
                characterDetailActivityRole.text = getString(R.string.character_detail_activity_role_text, it.role)
                characterDetailActivityHouse.text = getString(R.string.character_detail_activity_house_text, it.house)
                characterDetailActivityMinistryOfMagic.text =
                    getString(R.string.character_detail_activity_ministry_of_magic_text, getStringValueForDetail(it.ministryOfMagic))
                characterDetailActivityOrderOfThePhoenix.text =
                    getString(R.string.character_detail_activity_order_of_the_phoenix_text, getStringValueForDetail(it.orderOfThePhoenix))
                characterDetailActivityDumbledoresArmy.text =
                    getString(R.string.character_detail_activity_dumbledores_army_text, getStringValueForDetail(it.dumbledoresArmy))
                characterDetailActivityDeathEater.text =
                    getString(R.string.character_detail_activity_death_eater_text, getStringValueForDetail(it.deathEater))
                characterDetailActivityBloodStatus.text =
                    getString(R.string.character_detail_activity_blood_status_text, it.bloodStatus)
                characterDetailActivitySpecies.text =
                    getString(R.string.character_detail_activity_species_text, it.species)
            }
        }
    }

    private fun getStringValueForDetail(detail: Boolean): String =
        if (detail) getString(R.string.string_value_yes) else getString(R.string.string_value_no)

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, CharacterDetailActivity::class.java)
    }
}
