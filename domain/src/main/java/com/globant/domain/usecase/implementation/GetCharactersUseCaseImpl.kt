package com.globant.domain.usecase.implementation

import com.globant.domain.entity.Character
import com.globant.domain.service.CharactersService
import com.globant.domain.usecase.GetCharactersUseCase
import com.globant.domain.util.Result

class GetCharactersUseCaseImpl(private val charactersService: CharactersService) : GetCharactersUseCase {
    override operator fun invoke(houseName: String): Result<List<Character>> = charactersService.getCharactersByHouse(houseName)
}
