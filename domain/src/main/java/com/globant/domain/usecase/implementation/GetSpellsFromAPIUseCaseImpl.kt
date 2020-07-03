package com.globant.domain.usecase.implementation

import com.globant.domain.entity.Spell
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetSpellsFromAPIUseCase
import com.globant.domain.util.Result

class GetSpellsFromAPIUseCaseImpl(private val spellsService: SpellsService) : GetSpellsFromAPIUseCase {
    override fun invoke(): Result<List<Spell>> = spellsService.getSpellsFromAPI()
}
