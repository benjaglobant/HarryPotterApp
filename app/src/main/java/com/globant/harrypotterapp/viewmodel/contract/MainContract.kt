package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.MainData
import kotlinx.coroutines.Job

interface MainContract {
    interface ViewModel {
        fun getHousesLiveData(): LiveData<Event<MainData>>
        fun fetchHouses(): Job
        fun goToHouse(houseName: String)
        fun goToSpells()
    }
}
