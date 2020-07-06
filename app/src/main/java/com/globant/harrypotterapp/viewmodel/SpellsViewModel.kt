package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.entity.Spell
import com.globant.domain.usecase.GetSpellsFromAPIUseCase
import com.globant.domain.usecase.GetSpellsFromDataBaseUseCase
import com.globant.domain.usecase.UpdateSpellsDataBaseUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Event
import com.globant.harrypotterapp.util.Status
import com.globant.harrypotterapp.viewmodel.contract.SpellsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpellsViewModel(
    private val getSpellsFromAPIUseCase: GetSpellsFromAPIUseCase,
    private val getSpellsFromDataBaseUseCase: GetSpellsFromDataBaseUseCase,
    private val updateSpellsDataBaseUseCase: UpdateSpellsDataBaseUseCase
) : ViewModel(), SpellsContract.ViewModel {

    private val spellsMutableLiveData = MutableLiveData<Event<Data<List<Spell>>>>()
    override fun getSpellsLiveData(): LiveData<Event<Data<List<Spell>>>> = spellsMutableLiveData

    override fun fetchSpells() = viewModelScope.launch {
        spellsMutableLiveData.postValue(Event(Data(status = Status.LOADING)))
        withContext(Dispatchers.IO) { getSpellsFromAPIUseCase.invoke() }.let { result ->
            when (result) {
                is Result.Success -> {
                    withContext(Dispatchers.IO) { updateSpellsDataBaseUseCase.invoke(result.data) }
                    spellsMutableLiveData.postValue(Event(Data(status = Status.SUCCESS, data = result.data)))
                }
                is Result.Failure -> {
                    withContext(Dispatchers.Default) { getSpellsFromDataBaseUseCase.invoke() }.let { databaseResult ->
                        when (databaseResult) {
                            is Result.Success -> {
                                spellsMutableLiveData.postValue(Event(Data(status = Status.SUCCESS, data = databaseResult.data)))
                            }
                            is Result.Failure -> {
                                spellsMutableLiveData.postValue(Event(Data(status = Status.ERROR, error = databaseResult.exception)))
                            }
                        }
                    }
                }
            }
        }
    }
}
