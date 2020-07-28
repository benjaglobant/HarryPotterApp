package com.globant.domain.entity

data class CharacterDetail(
    val id: String,
    val name: String,
    val role: String,
    val house: String,
    val ministryOfMagic: Boolean,
    val orderOfThePhoenix: Boolean,
    val dumbledoresArmy: Boolean,
    val deathEater: Boolean,
    val bloodStatus: String,
    val species: String
)
