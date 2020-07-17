package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.HouseDetail
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import kotlinx.coroutines.Job

interface HouseDetailContract {
    interface ViewModel {
        fun getHouseDetailLiveData(): LiveData<Event<Data<List<HouseDetail>>>>
        fun fetchHouseDetail(houseName: String): Job
    }
}
