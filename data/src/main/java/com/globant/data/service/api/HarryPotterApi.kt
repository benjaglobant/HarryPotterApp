package com.globant.data.service.api

import com.globant.data.service.response.HouseDetailResponse
import com.globant.data.service.response.HouseResponse
import com.globant.data.service.response.SpellResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HarryPotterApi {
    @GET("v1/spells")
    fun getSpells(): Call<List<SpellResponse>>

    @GET("v1/houses")
    fun getHouses(): Call<List<HouseResponse>>

    @GET("v1/houses/{houseId}")
    fun getHouseDetail(@Path("houseId") houseId: String): Call<List<HouseDetailResponse>>
}
