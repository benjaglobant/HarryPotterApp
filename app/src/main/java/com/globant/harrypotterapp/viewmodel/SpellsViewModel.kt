package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.harrypotterapp.ListOfSpellsMocked
import com.globant.harrypotterapp.viewmodel.contract.SpellsContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpellsViewModel : ViewModel(), SpellsContract.ViewModel {

    private val spellsMutableLiveData = MutableLiveData<SpellsData>()
    override fun getSpellsLiveData(): LiveData<SpellsData> = spellsMutableLiveData

    override fun fetchSpells() = viewModelScope.launch {

        spellsMutableLiveData.value = SpellsData(SpellsState.SHOW_LOADER)
        delay(LOADER_TIME)
        spellsMutableLiveData.value = SpellsData(SpellsState.SHOW_DATA, ListOfSpellsMocked())
        // TODO: Implement this method using use cases for fetch spells from API
    }

    companion object {
        private const val LOADER_TIME = 1500L
    }
}

data class SpellsData(val state: SpellsState, val data: ListOfSpellsMocked? = null)
enum class SpellsState { SHOW_LOADER, SHOW_DATA }
