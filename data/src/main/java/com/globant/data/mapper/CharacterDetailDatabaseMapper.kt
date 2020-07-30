package com.globant.data.mapper

import com.globant.data.entity.CharacterDetailEntity
import com.globant.domain.entity.CharacterDetail

class CharacterDetailDatabaseMapper : BaseMapper<CharacterDetailEntity, CharacterDetail> {

    override fun transform(type: CharacterDetailEntity): CharacterDetail = type.run {
        CharacterDetail(
            id,
            name,
            role,
            house,
            ministryOfMagic,
            orderOfThePhoenix,
            dumbledoresArmy,
            deathEater,
            bloodStatus,
            species
        )
    }

    fun transformToData(type: CharacterDetail): CharacterDetailEntity = type.run {
        CharacterDetailEntity(
            id,
            name,
            role,
            house,
            ministryOfMagic,
            orderOfThePhoenix,
            dumbledoresArmy,
            deathEater,
            bloodStatus,
            species
        )
    }

    fun transformToListOfCharacterDetail(charactersDetails: List<CharacterDetailEntity>): List<CharacterDetail> =
        charactersDetails.map { transform(it) }
}
