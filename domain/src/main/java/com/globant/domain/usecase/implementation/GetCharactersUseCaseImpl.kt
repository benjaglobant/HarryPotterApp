package com.globant.domain.usecase.implementation

import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.Character
import com.globant.domain.service.CharactersService
import com.globant.domain.usecase.GetCharactersUseCase
import com.globant.domain.util.Result

class GetCharactersUseCaseImpl(
    private val charactersService: CharactersService,
    private val database: HarryPotterDataBase
) : GetCharactersUseCase {
    override operator fun invoke(houseName: String): Result<List<Character>> {
        return when (val serviceResult = charactersService.getCharactersByHouse(houseName)) {
            is Result.Success -> {
                database.updateCharacters(serviceResult.data, houseName)
                serviceResult
            }
            is Result.Failure -> {
                database.getCharactersByHouseName(houseName)
            }
        }
    }
}
