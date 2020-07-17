package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.HouseDetail
import com.globant.domain.usecase.GetHouseDetailByIdUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.util.Status
import com.globant.harrypotterapp.viewmodel.contract.HouseDetailContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HouseDetailViewModel(
    private val getHouseDetailByIdUseCase: GetHouseDetailByIdUseCase
) : ViewModel(), HouseDetailContract.ViewModel {

    private val houseDetailMutableLiveData = MutableLiveData<Event<Data<List<HouseDetail>>>>()
    override fun getHouseDetailLiveData(): LiveData<Event<Data<List<HouseDetail>>>> = houseDetailMutableLiveData

    override fun fetchHouseDetail(houseName: String) = viewModelScope.launch {
        houseDetailMutableLiveData.postValue(Event(Data(status = Status.LOADING)))
        withContext(Dispatchers.IO) { getHouseDetailByIdUseCase(houseName) }.let { result ->
            when (result) {
                is Result.Success -> {
                    houseDetailMutableLiveData.postValue(Event(Data(status = Status.SUCCESS, data = result.data)))
                }
                is Result.Failure -> {
                    houseDetailMutableLiveData.postValue(Event(Data(status = Status.ERROR, error = result.exception)))
                }
            }
        }
    }
}
