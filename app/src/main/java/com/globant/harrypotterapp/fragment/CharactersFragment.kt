package com.globant.harrypotterapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.domain.entity.Character
import com.globant.harrypotterapp.activity.CharacterDetailActivity
import com.globant.harrypotterapp.adapter.CharactersAdapter
import com.globant.harrypotterapp.adapter.OnCharacterClicked
import com.globant.harrypotterapp.databinding.FragmentCharactersBinding
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.CharactersData
import com.globant.harrypotterapp.viewmodel.CharactersStatus
import com.globant.harrypotterapp.viewmodel.CharactersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment(), OnCharacterClicked {

    private lateinit var binding: FragmentCharactersBinding
    private val charactersAdapter = CharactersAdapter(this)
    private val charactersViewModel by viewModel<CharactersViewModel>()
    private lateinit var houseName: String
    private lateinit var characterIdValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCharactersBinding.inflate(layoutInflater)
        charactersViewModel.getCharactersLiveData().observe(::getLifecycle, ::updateUI)
        arguments?.getString(HOUSE_NAME)?.let { houseName = it }
        charactersViewModel.fetchCharacters(houseName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root

    private fun updateUI(data: Event<CharactersData<List<Character>>>) {
        val charactersData = data.getContentIfNotHandled()
        when (charactersData?.status) {
            CharactersStatus.LOADING_CHARACTERS -> {
                binding.charactersFragmentLoader.visibility = View.VISIBLE
            }
            CharactersStatus.SUCCESS_CHARACTERS -> {
                showCharactersData(charactersData.data)
            }
            CharactersStatus.ERROR_CHARACTERS -> {
                showCharactersErrorMessage(charactersData.error?.message)
            }
            CharactersStatus.OPEN_CHARACTER_DETAIL -> {
                openCharacterDetail(characterIdValue)
            }
        }
    }

    private fun showCharactersData(characters: List<Character>?) {
        binding.charactersFragmentLoader.visibility = View.GONE
        characters?.let { charactersAdapter.submitList(it) }
        binding.charactersFragmentRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.charactersFragmentRecyclerView.adapter = charactersAdapter
    }

    private fun showCharactersErrorMessage(message: String?) {
        binding.charactersFragmentLoader.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCharacterClicked(characterId: String) {
        characterIdValue = characterId
        charactersViewModel.onCharacterClicked()
    }

    private fun openCharacterDetail(characterId: String) {
        this.context?.let { startActivity(CharacterDetailActivity.getIntent(it, characterId)) }
    }

    companion object {
        private const val HOUSE_NAME = "houseName"
        fun getInstance(houseName: String): CharactersFragment {
            val args = Bundle()
            args.putString(HOUSE_NAME, houseName)
            val fragment = CharactersFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
