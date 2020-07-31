package com.globant.data.mapper

import com.globant.data.entity.CharacterEntity
import com.globant.domain.entity.Character

class CharacterDatabaseMapper : BaseMapper<CharacterEntity, Character> {
    override fun transform(type: CharacterEntity): Character = type.run {
        Character(
            this.id,
            this.name,
            this.role
        )
    }

    fun transformToData(type: Character, houseName: String): CharacterEntity = type.run {
        CharacterEntity(
            this.id,
            this.name,
            this.role ?: UNKNOWN_ROLE,
            houseName
        )
    }

    fun transformToListOfCharacter(characters: List<CharacterEntity>): List<Character> =
        characters.map { transform(it) }

    companion object {
        private const val UNKNOWN_ROLE = "Unknown"
    }
}
