package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.Character
import com.globant.domain.usecase.GetCharactersUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.contract.CharactersContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(private val getCharactersUseCase: GetCharactersUseCase) : ViewModel(), CharactersContract.ViewModel {

    private val charactersMutableLiveData = MutableLiveData<Event<CharactersData<List<Character>>>>()
    override fun getCharactersLiveData(): LiveData<Event<CharactersData<List<Character>>>> = charactersMutableLiveData

    override fun fetchCharacters(houseName: String) = viewModelScope.launch {
        charactersMutableLiveData.postValue(Event(CharactersData(status = CharactersStatus.LOADING_CHARACTERS)))
        withContext(Dispatchers.IO) { getCharactersUseCase(houseName) }.let { result ->
            when (result) {
                is Result.Success -> {
                    charactersMutableLiveData.postValue(
                        Event(CharactersData(status = CharactersStatus.SUCCESS_CHARACTERS, data = result.data))
                    )
                }
                is Result.Failure -> {
                    charactersMutableLiveData.postValue(
                        Event(CharactersData(status = CharactersStatus.ERROR_CHARACTERS, error = result.exception))
                    )
                }
            }
        }
    }
}

data class CharactersData<RequestData>(var status: CharactersStatus, var data: RequestData? = null, var error: Exception? = null)

enum class CharactersStatus {
    LOADING_CHARACTERS,
    SUCCESS_CHARACTERS,
    ERROR_CHARACTERS
}
