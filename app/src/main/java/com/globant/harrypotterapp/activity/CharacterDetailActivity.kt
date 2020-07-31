package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.globant.domain.entity.CharacterDetail
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.databinding.ActivityCharacterDetailBinding
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.CharacterDetailData
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.ERROR_CHARACTER_DETAILS
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.LOADING_CHARACTER_DETAILS
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.SUCCESS_CHARACTER_DETAILS
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

        intent.extras?.getString(CHARACTER_ID)?.let { characterDetailViewModel.fetchCharacterDetail(it) }
    }

    private fun updateUI(data: Event<CharacterDetailData<CharacterDetail>>) {
        val characterDetail = data.getContentIfNotHandled()
        when (characterDetail?.status) {
            LOADING_CHARACTER_DETAILS -> {
                binding.characterDetailActivityLoader.visibility = View.VISIBLE
            }
            SUCCESS_CHARACTER_DETAILS -> {
                showCharacterDetails(characterDetail.data)
            }
            ERROR_CHARACTER_DETAILS -> {
                showCharacterDetailError(characterDetail.error?.message)
            }
        }
    }

    private fun showCharacterDetails(details: CharacterDetail?) {
        with(binding) {
            details?.let {
                characterDetailActivityLoader.visibility = View.GONE
                characterDetailActivityToolbar.title = it.name
                characterDetailActivityRole.apply {
                    text = getString(R.string.character_detail_activity_role_text, it.role)
                    visibility = View.VISIBLE
                }
                characterDetailActivityHouse.apply {
                    text = getString(R.string.character_detail_activity_house_text, it.house)
                    visibility = View.VISIBLE
                }
                characterDetailActivityMinistryOfMagic.apply {
                    text = getString(R.string.character_detail_activity_ministry_of_magic_text, it.ministryOfMagic)
                    visibility = View.VISIBLE
                }
                characterDetailActivityOrderOfThePhoenix.apply {
                    text = getString(R.string.character_detail_activity_order_of_the_phoenix_text, it.orderOfThePhoenix)
                    visibility = View.VISIBLE
                }
                characterDetailActivityDumbledoresArmy.apply {
                    text = getString(R.string.character_detail_activity_dumbledores_army_text, it.dumbledoresArmy)
                    visibility = View.VISIBLE
                }
                characterDetailActivityDeathEater.apply {
                    text = getString(R.string.character_detail_activity_death_eater_text, it.deathEater)
                    visibility = View.VISIBLE
                }
                characterDetailActivityBloodStatus.apply {
                    text = getString(R.string.character_detail_activity_blood_status_text, it.bloodStatus)
                    visibility = View.VISIBLE
                }
                characterDetailActivitySpecies.apply {
                    text = getString(R.string.character_detail_activity_species_text, it.species)
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showCharacterDetailError(message: String?) {
        with(binding) {
            characterDetailActivityLoader.visibility = View.GONE
            characterDetailActivityToolbar.title = getString(R.string.error_title)
            characterDetailActivityError.visibility = View.VISIBLE
            characterDetailActivityError.text =
                getString(R.string.error_message_text, message, CHARACTER_DETAILS)
        }
    }

    companion object {
        private const val CHARACTER_ID = "characterId"
        private const val CHARACTER_DETAILS = "Character details"
        fun getIntent(context: Context, characterId: String): Intent =
            Intent(context, CharacterDetailActivity::class.java).putExtra(CHARACTER_ID, characterId)
    }
}
