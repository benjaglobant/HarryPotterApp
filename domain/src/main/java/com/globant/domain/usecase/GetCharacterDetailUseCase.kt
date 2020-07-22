package com.globant.domain.usecase

import com.globant.domain.entity.CharacterDetail
import com.globant.domain.util.Result

interface GetCharacterDetailUseCase {
    operator fun invoke(characterId: String): Result<CharacterDetail>
}
