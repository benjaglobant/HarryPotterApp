package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Constants.EMPTY_STRING
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.contract.MainContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val getHousesUseCase: GetHousesUseCase) : ViewModel(), MainContract.ViewModel {

    private val housesMutableLiveData = MutableLiveData<Event<MainData>>()
    override fun getHousesLiveData(): LiveData<Event<MainData>> = housesMutableLiveData

    override fun fetchHouses(): Job = viewModelScope.launch {
        housesMutableLiveData.postValue(Event(MainData(status = MainStatus.LOADING_MAIN)))
        withContext(Dispatchers.IO) { getHousesUseCase() }.let { result ->
            when (result) {
                is Result.Success -> {
                    housesMutableLiveData.postValue(Event(MainData(status = MainStatus.SUCCESS_MAIN)))
                }
                is Result.Failure -> {
                    housesMutableLiveData.postValue(Event(MainData(status = MainStatus.ERROR_MAIN, error = result.exception)))
                }
            }
        }
    }

    override fun goToHouse(houseName: String) {
        housesMutableLiveData.value = Event(MainData(status = MainStatus.GO_HOUSE, houseName = houseName))
    }

    override fun goToSpells() {
        housesMutableLiveData.value = Event(MainData(status = MainStatus.GO_SPELLS))
    }
}

data class MainData(var status: MainStatus, var houseName: String = EMPTY_STRING, var error: Exception? = null)

enum class MainStatus {
    LOADING_MAIN,
    SUCCESS_MAIN,
    ERROR_MAIN,
    GO_HOUSE,
    GO_SPELLS
}
