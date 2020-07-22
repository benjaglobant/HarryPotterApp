package com.globant.data.service.response

import com.google.gson.annotations.SerializedName

data class HouseResponse(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String
)
