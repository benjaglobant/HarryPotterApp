package com.globant.domain.usecase

import com.globant.domain.entity.Spell

interface UpdateSpellsDataBaseUseCase {
    fun invoke(spells: List<Spell>)
}
