package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.domain.entity.Spell
import com.globant.harrypotterapp.adapter.SpellsAdapter
import com.globant.harrypotterapp.databinding.ActivitySpellsBinding
import com.globant.harrypotterapp.viewmodel.SpellsData
import com.globant.harrypotterapp.viewmodel.SpellsState
import com.globant.harrypotterapp.viewmodel.SpellsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpellsActivity : AppCompatActivity() {

    private val spellsViewModel by viewModel<SpellsViewModel>()
    private val spellsAdapter = SpellsAdapter()
    private lateinit var binding: ActivitySpellsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpellsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        spellsViewModel.getSpellsLiveData().observe(::getLifecycle, ::updateUI)
        spellsViewModel.fetchSpells()
    }

    private fun updateUI(spells: SpellsData) {
        when (spells.state) {
            SpellsState.SHOW_LOADER -> binding.spellsActivityLoader.visibility = View.VISIBLE
            SpellsState.SHOW_DATA -> spells.data?.listOfSpells?.let { showSpellsData(it) }
        }
    }

    private fun showSpellsData(spells: List<Spell>) {
        binding.spellsActivityLoader.visibility = View.GONE
        spellsAdapter.submitList(spells)
        binding.spellsActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.spellsActivityRecyclerView.adapter = spellsAdapter
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, SpellsActivity::class.java)
    }
}
