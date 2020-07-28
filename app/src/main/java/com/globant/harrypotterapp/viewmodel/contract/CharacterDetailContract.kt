package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.CharacterDetail
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.CharacterDetailData

interface CharacterDetailContract {
    interface ViewModel {
        fun getCharacterDetailLiveData(): LiveData<Event<CharacterDetailData<CharacterDetail>>>
        fun fetchCharacterDetail()
    }
}
