package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.domain.entity.Spell
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.SpellData
import kotlinx.coroutines.Job

interface SpellsContract {
    interface ViewModel {
        fun getSpellsLiveData(): LiveData<Event<SpellData<List<Spell>>>>
        fun fetchSpells(): Job
    }
}
