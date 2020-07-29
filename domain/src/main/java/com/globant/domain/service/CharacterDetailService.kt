package com.globant.domain.service

import com.globant.domain.entity.CharacterDetail
import com.globant.domain.util.Result

interface CharacterDetailService {
    fun getCharacterDetail(characterId: String): Result<CharacterDetail>
}
