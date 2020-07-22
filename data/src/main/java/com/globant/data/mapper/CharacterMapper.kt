package com.globant.data.mapper

import com.globant.data.service.response.CharacterResponse
import com.globant.domain.entity.Character

class CharacterMapper : BaseMapper<CharacterResponse, Character> {
    override fun transform(type: CharacterResponse): Character = type.run {
        Character(
            this.id,
            this.name,
            this.role?.let { it } ?: UNKNOWN_ROLE
        )
    }

    fun transformListOfCharacters(charactersResponse: List<CharacterResponse>) = charactersResponse.map { transform(it) }

    companion object {
        private const val UNKNOWN_ROLE = "Unknown"
    }
}
