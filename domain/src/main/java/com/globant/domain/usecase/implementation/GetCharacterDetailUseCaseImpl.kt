package com.globant.domain.usecase.implementation

import com.globant.domain.entity.CharacterDetail
import com.globant.domain.service.CharacterDetailService
import com.globant.domain.usecase.GetCharacterDetailUseCase
import com.globant.domain.util.Result

class GetCharacterDetailUseCaseImpl(private val characterDetailService: CharacterDetailService) : GetCharacterDetailUseCase {
    override fun invoke(characterId: String): Result<CharacterDetail> = characterDetailService.getCharacterDetail(characterId)
}
