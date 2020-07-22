package com.globant.domain.usecase

import com.globant.domain.entity.Character
import com.globant.domain.util.Result
import org.koin.core.KoinComponent

interface GetCharactersUseCase : KoinComponent {
    operator fun invoke(houseName: String): Result<List<Character>>
}
