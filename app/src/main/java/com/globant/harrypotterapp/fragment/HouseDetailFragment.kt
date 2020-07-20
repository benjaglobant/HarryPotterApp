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
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.HouseDetailData
import com.globant.harrypotterapp.viewmodel.HouseDetailStatus
import com.globant.harrypotterapp.viewmodel.HouseDetailViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HouseDetailFragment : Fragment() {

    private val houseDetailViewModel by sharedViewModel<HouseDetailViewModel>()
    private lateinit var binding: FragmentHouseDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHouseDetailBinding.inflate(layoutInflater)
        houseDetailViewModel.getHouseDetailLiveData().observe(::getLifecycle, ::updateUI)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        binding.root

    private fun updateUI(data: Event<HouseDetailData<List<HouseDetail>>>) {
        val houseDetailData = data.getContentIfNotHandled()
        when (houseDetailData?.status) {
            HouseDetailStatus.LOADING_HOUSE_DETAIL -> binding.houseDetailFragmentLoader.visibility = View.VISIBLE
            HouseDetailStatus.SUCCESS_HOUSE_DETAIL -> showHouseDetailData(houseDetailData.data?.get(ZERO))
            HouseDetailStatus.ERROR_HOUSE_DETAIL -> showErrorMessage()
        }
    }

    private fun showHouseDetailData(houseDetail: HouseDetail?) {
        binding.houseDetailFragmentLoader.visibility = View.GONE
        houseDetail?.apply {
            binding.houseDetailFragmentName.text = getString(R.string.house_detail_fragment_name_text, name)
            binding.houseDetailFragmentMascot.text = getString(R.string.house_detail_fragment_mascot_text, mascot)
            binding.houseDetailFragmentHeadOfHouse.text = getString(R.string.house_detail_fragment_head_of_house_text, headOfHouse)
            binding.houseDetailFragmentHouseGhost.text = getString(R.string.house_detail_fragment_ghost_text, houseGhost)
            binding.houseDetailFragmentFounder.text = getString(R.string.house_detail_fragment_founder_text, founder)
            when (name) {
                GRYFFINDOR -> binding.houseDetailFragmentImage.setImageResource(R.drawable.gryffindor)
                RAVENCLAW -> binding.houseDetailFragmentImage.setImageResource(R.drawable.ravenclaw)
                SLYTHERIN -> binding.houseDetailFragmentImage.setImageResource(R.drawable.slytherin)
                HUFFLEPUFF -> binding.houseDetailFragmentImage.setImageResource(R.drawable.hufflepuff)
                else -> showErrorMessage()
            }
        }
    }

    private fun showErrorMessage() {
        binding.houseDetailFragmentLoader.visibility = View.GONE
        binding.houseDetailFragmentImage.setImageResource(R.drawable.hogwarts)
        Toast.makeText(this.context, getString(R.string.house_detail_fragment_error_message), Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ZERO = 0
        private const val GRYFFINDOR = "Gryffindor"
        private const val RAVENCLAW = "Ravenclaw"
        private const val SLYTHERIN = "Slytherin"
        private const val HUFFLEPUFF = "Hufflepuff"
    }
}
