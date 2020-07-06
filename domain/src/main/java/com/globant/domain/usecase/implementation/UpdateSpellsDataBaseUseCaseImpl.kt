package com.globant.domain.usecase.implementation

import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.entity.Spell
import com.globant.domain.usecase.UpdateSpellsDataBaseUseCase

class UpdateSpellsDataBaseUseCaseImpl(private val database: HarryPotterRoomDataBase) : UpdateSpellsDataBaseUseCase {
    override fun invoke(spells: List<Spell>) { database.updateSpells(spells) }
}
