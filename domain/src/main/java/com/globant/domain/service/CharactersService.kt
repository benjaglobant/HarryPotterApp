package com.globant.domain.service

import com.globant.domain.entity.Character
import com.globant.domain.util.Result

interface CharactersService {
    fun getCharactersByHouse(houseName: String): Result<List<Character>>
}
