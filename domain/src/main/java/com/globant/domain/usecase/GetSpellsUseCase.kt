package com.globant.domain.usecase

import com.globant.domain.entity.Spell
import com.globant.domain.util.Result

interface GetSpellsUseCase {
    fun invoke(): Result<List<Spell>>
}
