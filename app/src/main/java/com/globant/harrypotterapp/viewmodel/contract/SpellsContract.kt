package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.Spell
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import kotlinx.coroutines.Job

interface SpellsContract {
    interface ViewModel {
        fun getSpellsLiveData(): LiveData<Event<Data<List<Spell>>>>
        fun fetchSpells(): Job
    }
}
