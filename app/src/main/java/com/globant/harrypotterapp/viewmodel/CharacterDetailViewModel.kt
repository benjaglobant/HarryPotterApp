package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.CharacterDetail
import com.globant.domain.usecase.GetCharacterDetailUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.contract.CharacterDetailContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailViewModel(private val getCharacterDetailUseCase: GetCharacterDetailUseCase) :
    ViewModel(), CharacterDetailContract.ViewModel {

    private val characterDetailMutableLiveData = MutableLiveData<Event<CharacterDetailData<CharacterDetail>>>()

    override fun getCharacterDetailLiveData(): LiveData<Event<CharacterDetailData<CharacterDetail>>> =
        characterDetailMutableLiveData

    override fun fetchCharacterDetail(characterId: String) = viewModelScope.launch {
        characterDetailMutableLiveData.postValue(Event(CharacterDetailData(status = CharacterDetailStatus.LOADING_CHARACTER_DETAILS)))
        withContext(Dispatchers.IO) { getCharacterDetailUseCase(characterId) }.let { result ->
            when (result) {
                is Result.Success -> {
                    characterDetailMutableLiveData.postValue(
                        Event(
                            CharacterDetailData(
                                status = CharacterDetailStatus.SUCCESS_CHARACTER_DETAILS, data = result.data
                            )
                        )
                    )
                }
                is Result.Failure -> {
                    characterDetailMutableLiveData.postValue(
                        Event(
                            CharacterDetailData(
                                status = CharacterDetailStatus.ERROR_CHARACTER_DETAILS, error = result.exception
                            )
                        )
                    )
                }
            }
        }
    }
}

data class CharacterDetailData<RequestData>(var status: CharacterDetailStatus, var data: RequestData? = null, var error: Exception? = null)

enum class CharacterDetailStatus {
    LOADING_CHARACTER_DETAILS,
    SUCCESS_CHARACTER_DETAILS,
    ERROR_CHARACTER_DETAILS
}
