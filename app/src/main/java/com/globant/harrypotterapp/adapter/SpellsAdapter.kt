package com.globant.harrypotterapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globant.domain.entity.Spell
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.databinding.ActivitySpellsCardViewBinding

class SpellsAdapter : RecyclerView.Adapter<SpellsAdapter.ViewHolder>() {

    private val spells = mutableListOf<Spell>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_spells_card_view,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(spells[position])
    }

    override fun getItemCount(): Int = spells.size

    fun submitList(spellsList: List<Spell>) {
        spells.addAll(spellsList)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ActivitySpellsCardViewBinding.bind(itemView)
        fun bind(item: Spell) = item.let {
            binding.apply {
                activitySpellsCardViewSpell.text = it.spell
                activitySpellsCardViewType.text = it.type
                activitySpellsCardViewEffect.text = it.effect
            }
        }
    }
}
