package com.globant.domain.usecase.implementation

import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.Spell
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetSpellsUseCase
import com.globant.domain.util.Result

class GetSpellsUseCaseImpl(
    private val spellsService: SpellsService,
    private val database: HarryPotterDataBase
) : GetSpellsUseCase {
    override fun invoke(): Result<List<Spell>> =
        when (val result = spellsService.getSpells()) {
            is Result.Success -> {
                database.updateSpells(result.data)
                result
            }
            is Result.Failure -> {
                database.getSpellsFromDataBase()
            }
        }
}
