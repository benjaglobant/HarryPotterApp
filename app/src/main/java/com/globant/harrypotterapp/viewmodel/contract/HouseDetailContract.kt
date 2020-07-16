package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.HouseDetail

interface HouseDetailContract {
    interface ViewModel {
        fun getHouseDetailLiveData(): LiveData<HouseDetail>
        fun fetchHouseDetail()
    }
}
