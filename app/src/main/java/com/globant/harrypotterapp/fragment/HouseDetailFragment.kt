package com.globant.harrypotterapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.globant.domain.entity.HouseDetail
import com.globant.harrypotterapp.R
import com.globant.harrypotterapp.databinding.FragmentHouseDetailBinding
import com.globant.harrypotterapp.viewmodel.HouseDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HouseDetailFragment : Fragment() {

    private val houseDetailViewModel by viewModel<HouseDetailViewModel>()
    private lateinit var binding: FragmentHouseDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHouseDetailBinding.inflate(layoutInflater)
        houseDetailViewModel.getHouseDetailLiveData().observe(::getLifecycle, ::updateUI)
        houseDetailViewModel.fetchHouseDetail()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        binding.root

    private fun updateUI(houseDetail: HouseDetail) {
        houseDetail.apply {
            binding.houseDetailFragmentName.text = getString(R.string.house_detail_fragment_name_text, this.name)
            binding.houseDetailFragmentMascot.text = getString(R.string.house_detail_fragment_mascot_text, this.mascot)
            binding.houseDetailFragmentHeadOfHouse.text = getString(R.string.house_detail_fragment_head_of_house_text, this.headOfHouse)
            binding.houseDetailFragmentHouseGhost.text = getString(R.string.house_detail_fragment_ghost_text, this.houseGhost)
            binding.houseDetailFragmentFounder.text = getString(R.string.house_detail_fragment_founder_text, this.founder)
            when (this.name) {
                GRYFFINDOR -> binding.houseDetailFragmentImage.setImageResource(R.drawable.gryffindor)
                RAVENCLAW -> binding.houseDetailFragmentImage.setImageResource(R.drawable.ravenclaw)
                SLYTHERIN -> binding.houseDetailFragmentImage.setImageResource(R.drawable.slytherin)
                HUFFLEPUFF -> binding.houseDetailFragmentImage.setImageResource(R.drawable.hufflepuff)
                else -> {
                    binding.houseDetailFragmentImage.setImageResource(R.drawable.hogwarts)
                    showErrorMessage()
                }
            }
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(this.context, getString(R.string.house_detail_fragment_error_message), Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val GRYFFINDOR = "Gryffindor"
        private const val RAVENCLAW = "Ravenclaw"
        private const val SLYTHERIN = "Slytherin"
        private const val HUFFLEPUFF = "Hufflepuff"
    }
}
