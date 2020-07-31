package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.domain.entity.Spell
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.adapter.SpellsAdapter
import com.globant.harrypotterapp.databinding.ActivitySpellsBinding
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.SpellData
import com.globant.harrypotterapp.viewmodel.SpellStatus
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

    private fun updateUI(data: Event<SpellData<List<Spell>>>) {
        val spellsData = data.getContentIfNotHandled()
        when (spellsData?.status) {
            SpellStatus.LOADING_SPELLS -> binding.spellsActivityLoader.visibility = View.VISIBLE
            SpellStatus.SUCCESS_SPELLS -> showSpellsData(spellsData.data)
            SpellStatus.ERROR_SPELLS -> showSpellsError(spellsData.error?.message)
        }
    }

    private fun showSpellsData(spells: List<Spell>?) {
        binding.spellsActivityLoader.visibility = View.GONE
        spells?.let { spellsAdapter.submitList(it) }
        binding.spellsActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.spellsActivityRecyclerView.adapter = spellsAdapter
    }

    private fun showSpellsError(message: String?) {
        with(binding) {
            spellsActivityLoader.visibility = View.GONE
            spellsActivityError.visibility = View.VISIBLE
            spellsActivityError.text = getString(R.string.error_message_text, message, getString(R.string.spells_text))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, SpellsActivity::class.java)
    }
}
