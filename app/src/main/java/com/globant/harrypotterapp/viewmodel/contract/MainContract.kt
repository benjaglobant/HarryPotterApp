package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.House
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.MainData
import kotlinx.coroutines.Job

interface MainContract {
    interface ViewModel {
        fun getHousesLiveData(): LiveData<Event<MainData<List<House>>>>
        fun fetchHouses(): Job
    }
}
