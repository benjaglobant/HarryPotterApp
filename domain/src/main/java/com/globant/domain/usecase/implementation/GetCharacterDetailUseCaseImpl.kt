package com.globant.domain.usecase.implementation

import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.CharacterDetail
import com.globant.domain.service.CharacterDetailService
import com.globant.domain.usecase.GetCharacterDetailUseCase
import com.globant.domain.util.Result

class GetCharacterDetailUseCaseImpl(
    private val characterDetailService: CharacterDetailService,
    private val database: HarryPotterDataBase
) : GetCharacterDetailUseCase {
    override fun invoke(characterId: String): Result<CharacterDetail> =
        when (val result = characterDetailService.getCharacterDetail(characterId)) {
            is Result.Success -> {
                database.updateCharacterDetail(result.data)
                result
            }
            is Result.Failure -> database.getCharacterDetail(characterId)
        }
}
