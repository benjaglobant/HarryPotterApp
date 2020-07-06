package com.globant.domain.usecase.implementation

import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.entity.Spell
import com.globant.domain.usecase.GetSpellsFromDataBaseUseCase
import com.globant.domain.util.Result

class GetSpellsFromDataBaseUseCaseImpl(private val database: HarryPotterRoomDataBase) : GetSpellsFromDataBaseUseCase {
    override fun invoke(): Result<List<Spell>> = database.getSpellsFromDataBase()
}
