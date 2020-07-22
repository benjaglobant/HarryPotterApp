package com.globant.harrypotterapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globant.harrypotterapp.R
import com.globant.domain.entity.Character
import com.globant.harrypotterapp.databinding.FragmentCharactersCardViewBinding

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    private val characters = mutableListOf<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_characters_card_view,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    fun submitList(characterList: List<Character>) {
        characters.addAll(characterList)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = FragmentCharactersCardViewBinding.bind(itemView)
        fun bind(item: Character) = with(itemView) {
            item.let {
                binding.apply {
                    this.characterName.text = resources.getString(R.string.characters_fragment_card_view_name_text, it.name)
                    this.characterRole.text = resources.getString(R.string.characters_fragment_card_view_role_text, it.role)
                }
            }
        }
    }
}
