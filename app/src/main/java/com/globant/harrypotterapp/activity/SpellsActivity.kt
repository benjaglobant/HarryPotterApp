package com.globant.harrypotterapp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.domain.entity.Spell
import com.globant.harrypotterapp.adapter.SpellsAdapter
import com.globant.harrypotterapp.databinding.ActivitySpellsBinding
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.util.Status
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

    private fun updateUI(data: Event<Data<List<Spell>>>) {
        when (data.peekContent().status) {
            Status.LOADING -> binding.spellsActivityLoader.visibility = View.VISIBLE
            Status.SUCCESS -> showSpellsData(data.peekContent().data)
            Status.ERROR -> showSpellsError(data.peekContent().error?.message)
        }
    }

    private fun showSpellsData(spells: List<Spell>?) {
        binding.spellsActivityLoader.visibility = View.GONE
        spells?.let { spellsAdapter.submitList(it) }
        binding.spellsActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.spellsActivityRecyclerView.adapter = spellsAdapter
    }

    private fun showSpellsError(error: String?) {
        binding.spellsActivityLoader.visibility = View.GONE
        error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, SpellsActivity::class.java)
    }
}
