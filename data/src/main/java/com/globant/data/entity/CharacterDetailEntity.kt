package com.globant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_detail_table")
class CharacterDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val role: String,
    val house: String,
    val ministryOfMagic: String,
    val orderOfThePhoenix: String,
    val dumbledoresArmy: String,
    val deathEater: String,
    val bloodStatus: String,
    val species: String
)
