package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.domain.entity.CharacterDetail
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.contract.CharacterDetailContract

class CharacterDetailViewModel : ViewModel(), CharacterDetailContract.ViewModel {

    private val characterDetailMutableLiveData = MutableLiveData<Event<CharacterDetailData<CharacterDetail>>>()

    override fun getCharacterDetailLiveData(): LiveData<Event<CharacterDetailData<CharacterDetail>>> =
        characterDetailMutableLiveData

    override fun fetchCharacterDetail() {
        characterDetailMutableLiveData.value = Event(
            CharacterDetailData(
                status = CharacterDetailStatus.SHOW_CHARACTER_DETAILS, data = CharacterDetail(
                    "1",
                    "Harry Potter",
                    "student",
                    "Gryffindor",
                    false,
                    true,
                    true,
                    false,
                    "half-blood",
                    "human"
                )
            )
        )
    }
}

data class CharacterDetailData<RequestData>(var status: CharacterDetailStatus, var data: RequestData? = null, var error: Exception? = null)

enum class CharacterDetailStatus {
    SHOW_CHARACTER_DETAILS
}
