package com.globant.data.service.response

import com.google.gson.annotations.SerializedName

data class HouseDetailResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("mascot")
    val mascot: String,
    @SerializedName("headOfHouse")
    val director: String,
    @SerializedName("houseGhost")
    val houseGhost: String,
    @SerializedName("founder")
    val founder: String
)
