package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.Spell
import com.globant.domain.usecase.GetSpellsUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.viewmodel.contract.SpellsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpellsViewModel(
    private val getSpellsUseCase: GetSpellsUseCase
) : ViewModel(), SpellsContract.ViewModel {

    private val spellsMutableLiveData = MutableLiveData<Event<SpellData<List<Spell>>>>()
    override fun getSpellsLiveData(): LiveData<Event<SpellData<List<Spell>>>> = spellsMutableLiveData

    override fun fetchSpells() = viewModelScope.launch {
        spellsMutableLiveData.postValue(Event(SpellData(status = SpellStatus.LOADING_SPELLS)))
        withContext(Dispatchers.IO) { getSpellsUseCase.invoke() }.let { result ->
            when (result) {
                is Result.Success -> {
                    spellsMutableLiveData.postValue(Event(SpellData(status = SpellStatus.SUCCESS_SPELLS, data = result.data)))
                }
                is Result.Failure -> {
                    spellsMutableLiveData.postValue(Event(SpellData(status = SpellStatus.ERROR_SPELLS, error = result.exception)))
                }
            }
        }
    }
}

data class SpellData<RequestData>(var status: SpellStatus, var data: RequestData? = null, var error: Exception? = null)

enum class SpellStatus {
    LOADING_SPELLS,
    SUCCESS_SPELLS,
    ERROR_SPELLS
}
