package com.globant.data.service.response

import com.google.gson.annotations.SerializedName

data class SpellResponse(
    @SerializedName("spell")
    val spell: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("effect")
    val effect: String
)
