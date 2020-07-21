package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.House
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.contract.MainContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val getHousesUseCase: GetHousesUseCase) : ViewModel(), MainContract.ViewModel {

    private val housesMutableLiveData = MutableLiveData<Event<MainData<List<House>>>>()
    override fun getHousesLiveData(): LiveData<Event<MainData<List<House>>>> = housesMutableLiveData

    override fun fetchHouses(): Job = viewModelScope.launch {
        housesMutableLiveData.postValue(Event(MainData(status = MainStatus.LOADING_MAIN)))
        withContext(Dispatchers.IO) { getHousesUseCase() }.let { result ->
            when (result) {
                is Result.Success -> {
                    housesMutableLiveData.postValue(Event(MainData(status = MainStatus.SUCCESS_MAIN, data = result.data)))
                }
                is Result.Failure -> {
                    housesMutableLiveData.postValue(Event(MainData(status = MainStatus.ERROR_MAIN, error = result.exception)))
                }
            }
        }
    }
}

data class MainData<RequestData>(var status: MainStatus, var data: RequestData? = null, var error: Exception? = null)

enum class MainStatus {
    LOADING_MAIN,
    SUCCESS_MAIN,
    ERROR_MAIN
}
