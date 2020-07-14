package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.HouseId
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import kotlinx.coroutines.Job

interface MainContract {
    interface ViewModel {
        fun getHousesIdLiveData(): LiveData<Event<Data<List<HouseId>>>>
        fun fetchHousesIds(): Job
    }
}
