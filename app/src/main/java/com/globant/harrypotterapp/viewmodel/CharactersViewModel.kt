package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.domain.entity.Character
import com.globant.harrypotterapp.viewmodel.contract.CharactersContract

class CharactersViewModel : ViewModel(), CharactersContract.ViewModel {

    private val charactersMutableLiveData = MutableLiveData<List<Character>>()
    override fun getCharactersLiveData(): LiveData<List<Character>> = charactersMutableLiveData

    override fun fetchCharacters() {
        charactersMutableLiveData.value = listOf(
            Character("1", "Rubeus Hagrid", "Groundkeeper, Professor, Care of Magical Creatures"),
            Character("1", "Bathsheda Babbling", "Professor, Ancient Runes"),
            Character("1", "Ludo Bagman", "Head, Department of Magical Games and Sports"),
            Character("1", "Bathilda Bagshot", "Author, A History Of Magic"),
            Character("1", "Katie Bell", "student"),
            Character("1", "Cuthbert Binns", "Professor, History of Magic"),
            Character("1", "Phineas Nigellus Black", "(Formerly) Headmaster of Hogwarts"),
            Character("1", "Amelia Bones", "Head, Department of Magical Law Enforcement"),
            Character("1", "Susan Bones", "Hogwarts School of Witchcraft and Wizardry"),
            Character("1", "Katie Bell", "student"),
            Character("1", "Mafalda Hopkirk", "Assistant, mproper Use of Magic Office")
        )
    }
}
