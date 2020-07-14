package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.HouseDetail
import com.globant.domain.usecase.GetHouseDetailByIdUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.contract.HouseDetailContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HouseDetailViewModel(
    private val getHouseDetailByIdUseCase: GetHouseDetailByIdUseCase
) : ViewModel(), HouseDetailContract.ViewModel {

    private val houseDetailMutableLiveData = MutableLiveData<Event<HouseDetailData<List<HouseDetail>>>>()
    override fun getHouseDetailLiveData(): LiveData<Event<HouseDetailData<List<HouseDetail>>>> = houseDetailMutableLiveData

    override fun fetchHouseDetail(houseName: String) = viewModelScope.launch {
        houseDetailMutableLiveData.postValue(Event(HouseDetailData(status = HouseDetailStatus.LOADING_HOUSE_DETAIL)))
        withContext(Dispatchers.IO) { getHouseDetailByIdUseCase(houseName) }.let { result ->
            when (result) {
                is Result.Success -> {
                    houseDetailMutableLiveData.postValue(
                        Event(HouseDetailData(status = HouseDetailStatus.SUCCESS_HOUSE_DETAIL, data = result.data))
                    )
                }
                is Result.Failure -> {
                    houseDetailMutableLiveData.postValue(
                        Event(HouseDetailData(status = HouseDetailStatus.ERROR_HOUSE_DETAIL, error = result.exception))
                    )
                }
            }
        }
    }
}

data class HouseDetailData<RequestData>(var status: HouseDetailStatus, var data: RequestData? = null, var error: Exception? = null)

enum class HouseDetailStatus {
    LOADING_HOUSE_DETAIL,
    SUCCESS_HOUSE_DETAIL,
    ERROR_HOUSE_DETAIL
}
