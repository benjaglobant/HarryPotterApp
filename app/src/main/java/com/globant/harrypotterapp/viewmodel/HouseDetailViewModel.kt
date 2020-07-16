package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.domain.entity.HouseDetail
import com.globant.harrypotterapp.viewmodel.contract.HouseDetailContract

class HouseDetailViewModel : ViewModel(), HouseDetailContract.ViewModel {

    private val houseDetailMutableLiveData = MutableLiveData<HouseDetail>()
    override fun getHouseDetailLiveData(): LiveData<HouseDetail> = houseDetailMutableLiveData

    override fun fetchHouseDetail() {
        houseDetailMutableLiveData.value = HouseDetail(
            "Gryffindor",
            "House Mascot",
            "Head of House",
            "House Ghost",
            "Founder"
        )
        // TODO: Replace this mocked data for the usecase to get the HouseDetail from the API.
    }
}
