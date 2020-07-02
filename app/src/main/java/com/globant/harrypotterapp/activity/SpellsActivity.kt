package com.globant.harrypotterapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.harrypotterapp.ListOfSpellsMocked
import com.globant.harrypotterapp.adapter.SpellsAdapter
import com.globant.harrypotterapp.databinding.ActivitySpellsBinding
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

    private fun updateUI(spells: ListOfSpellsMocked) {
        spellsAdapter.submitList(spells.listOfSpells)
        binding.spellsActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.spellsActivityRecyclerView.adapter = spellsAdapter
    }

    companion object {
        fun getIntent(activity: Activity): Intent = Intent(activity, SpellsActivity::class.java)
    }
}
