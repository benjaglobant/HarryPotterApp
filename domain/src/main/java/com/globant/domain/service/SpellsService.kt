package com.globant.domain.service

import com.globant.domain.entity.Spell
import com.globant.domain.util.Result

interface SpellsService {
    fun getSpells(): Result<List<Spell>>
}
