package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.HouseId
import com.globant.domain.usecase.GetHousesIdsUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.util.Status
import com.globant.harrypotterapp.viewmodel.contract.MainContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val getHousesIdsUseCase: GetHousesIdsUseCase) : ViewModel(), MainContract.ViewModel {

    private val housesIdMutableLiveData = MutableLiveData<Event<Data<List<HouseId>>>>()
    override fun getHousesIdLiveData(): LiveData<Event<Data<List<HouseId>>>> = housesIdMutableLiveData

    override fun fetchHousesIds(): Job = viewModelScope.launch {
        withContext(Dispatchers.IO) { getHousesIdsUseCase.invoke() }.let { result ->
            when (result) {
                is Result.Success -> {
                    housesIdMutableLiveData.postValue(Event(Data(status = Status.SUCCESS, data = result.data)))
                }
                is Result.Failure -> {
                    housesIdMutableLiveData.postValue(Event(Data(status = Status.ERROR, error = result.exception)))
                }
            }
        }
    }
}
