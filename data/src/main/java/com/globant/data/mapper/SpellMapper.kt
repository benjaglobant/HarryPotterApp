package com.globant.data.mapper

import com.globant.data.service.response.SpellResponse
import com.globant.domain.entity.Spell

class SpellMapper : BaseMapper<SpellResponse, Spell> {
    override fun transform(type: SpellResponse) = type.run {
        Spell(
            this.spell,
            this.type,
            this.effect
        )
    }

    fun transformListOfSpells(spellsResponse: List<SpellResponse>) = spellsResponse.map { transform(it) }
}
