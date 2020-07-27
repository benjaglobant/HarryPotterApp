package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.CharacterDetail

interface CharacterDetailContract {
    interface ViewModel {
        fun getCharacterDetailLiveData(): LiveData<CharacterDetail>
        fun fetchCharacterDetail()
    }
}
