package com.globant.harrypotterapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.domain.entity.Character
import com.globant.harrypotterapp.adapter.CharactersAdapter
import com.globant.harrypotterapp.databinding.FragmentCharactersBinding
import com.globant.harrypotterapp.viewmodel.CharactersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private val charactersAdapter = CharactersAdapter()
    private val charactersViewModel by viewModel<CharactersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCharactersBinding.inflate(layoutInflater)
        charactersViewModel.getCharactersLiveData().observe(::getLifecycle, ::updateUI)
        charactersViewModel.fetchCharacters()
    }

    private fun updateUI(data: List<Character>) {
        charactersAdapter.submitList(data)
        binding.charactersFragmentRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.charactersFragmentRecyclerView.adapter = charactersAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root
}
