package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.harrypotterapp.ListOfSpellsMocked
import com.globant.harrypotterapp.contract.SpellsContract
import kotlinx.coroutines.launch

class SpellsViewModel : ViewModel(), SpellsContract.ViewModel {

    private val spellsMutableLiveData = MutableLiveData<ListOfSpellsMocked>()
    override fun getSpellsLiveData(): LiveData<ListOfSpellsMocked> = spellsMutableLiveData

    override fun fetchSpells() = viewModelScope.launch {
        spellsMutableLiveData.value = ListOfSpellsMocked()
        // TODO: Implement this method using use cases for fetch spells from API
    }
}
