package com.globant.data.mapper

import com.globant.domain.entity.Spell
import com.globant.data.entity.SpellRoom

class SpellDataBaseMapper : BaseMapper<SpellRoom, Spell> {

    override fun transform(type: SpellRoom): Spell = type.run {
        Spell(
            this.spell,
            this.type,
            this.effect
        )
    }

    fun transformToData(type: Spell): SpellRoom = type.run {
        SpellRoom(
            this.spell,
            this.type,
            this.effect
        )
    }

    fun transformListOfSpells(spellsRoom: List<SpellRoom>) = spellsRoom.map { transform(it) }
}
