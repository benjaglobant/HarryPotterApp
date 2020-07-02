package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.harrypotterapp.viewmodel.SpellsData
import kotlinx.coroutines.Job

interface SpellsContract {
    interface ViewModel {
        fun getSpellsLiveData(): LiveData<SpellsData>
        fun fetchSpells(): Job
    }
}
