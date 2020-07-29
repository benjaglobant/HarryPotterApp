package com.globant.data.service.response

import com.google.gson.annotations.SerializedName

data class CharacterDetailResponse(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("role")
    val role: String?,
    @SerializedName("house")
    val house: String,
    @SerializedName("ministryOfMagic")
    val ministryOfMagic: Boolean?,
    @SerializedName("orderOfThePhoenix")
    val orderOfThePhoenix: Boolean?,
    @SerializedName("dumbledoresArmy")
    val dumbledoresArmy: Boolean?,
    @SerializedName("deathEater")
    val deathEater: Boolean?,
    @SerializedName("bloodStatus")
    val bloodStatus: String?,
    @SerializedName("species")
    val species: String?
)
