package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.Character

interface CharactersContract {
    interface ViewModel {
        fun getCharactersLiveData(): LiveData<List<Character>>
        fun fetchCharacters()
    }
}
