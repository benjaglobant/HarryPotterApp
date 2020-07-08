package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.Spell
import com.globant.domain.usecase.GetSpellsUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.util.Status
import com.globant.harrypotterapp.viewmodel.contract.SpellsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpellsViewModel(
    private val getSpellsUseCase: GetSpellsUseCase
) : ViewModel(), SpellsContract.ViewModel {

    private val spellsMutableLiveData = MutableLiveData<Event<Data<List<Spell>>>>()
    override fun getSpellsLiveData(): LiveData<Event<Data<List<Spell>>>> = spellsMutableLiveData

    override fun fetchSpells() = viewModelScope.launch {
        spellsMutableLiveData.postValue(Event(Data(status = Status.LOADING)))
        withContext(Dispatchers.IO) { getSpellsUseCase.invoke() }.let { result ->
            when (result) {
                is Result.Success -> {
                    spellsMutableLiveData.postValue(Event(Data(status = Status.SUCCESS, data = result.data)))
                }
                is Result.Failure -> {
                    spellsMutableLiveData.postValue(Event(Data(status = Status.ERROR, error = result.exception)))
                }
            }
        }
    }
}
