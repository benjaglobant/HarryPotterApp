package com.globant.data.service.api

import com.globant.data.service.response.SpellResponse
import retrofit2.Call
import retrofit2.http.GET

interface HarryPotterApi {
    @GET("v1/spells")
    fun getSpells(): Call<List<SpellResponse>>
}
