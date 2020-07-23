package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.Character
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.CharactersData
import kotlinx.coroutines.Job

interface CharactersContract {
    interface ViewModel {
        fun getCharactersLiveData(): LiveData<Event<CharactersData<List<Character>>>>
        fun fetchCharacters(houseName: String): Job
    }
}
