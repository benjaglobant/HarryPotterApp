package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.domain.entity.CharacterDetail
import com.globant.harrypotterapp.viewmodel.contract.CharacterDetailContract

class CharacterDetailViewModel : ViewModel(), CharacterDetailContract.ViewModel {

    private val characterDetailMutableLiveData = MutableLiveData<CharacterDetail>()

    override fun getCharacterDetailLiveData(): LiveData<CharacterDetail> =
        characterDetailMutableLiveData

    override fun fetchCharacterDetail() {
        characterDetailMutableLiveData.value = CharacterDetail(
            "1",
            "Harry Potter",
            "student",
            "Gryffindor",
            false,
            true,
            true,
            false,
            "half-blood",
            "human"
        )
    }
}
