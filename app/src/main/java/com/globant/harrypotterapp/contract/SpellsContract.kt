package com.globant.harrypotterapp.contract

import androidx.lifecycle.LiveData
import com.globant.harrypotterapp.ListOfSpellsMocked
import kotlinx.coroutines.Job

interface SpellsContract {
    interface ViewModel {
        fun getSpellsLiveData(): LiveData<ListOfSpellsMocked>
        fun fetchSpells(): Job
    }
}
