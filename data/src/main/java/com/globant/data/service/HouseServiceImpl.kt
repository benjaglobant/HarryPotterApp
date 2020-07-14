package com.globant.data.service

import com.globant.data.mapper.HouseMapper
import com.globant.data.service.api.HarryPotterApi
import com.globant.domain.entity.House
import com.globant.domain.service.HouseService
import com.globant.domain.util.Result

class HouseServiceImpl : HouseService {

    private val api = ServiceGenerator()
    private val mapper = HouseMapper()

    override fun getHousesId(): Result<List<House>> {
        try {
            val callResponse = api.createService(HarryPotterApi::class.java).getHousesId()
            val response = callResponse.execute()
            if (response.isSuccessful)
                response.body()?.let {
                    mapper.transformListOfHouseId(it)
                }?.let {
                    return Result.Success(it)
                }
        } catch (e: Exception) {
            return Result.Failure(Exception(NOT_FOUND))
        }
        return Result.Failure(Exception(NOT_FOUND))
    }

    companion object {
        private const val NOT_FOUND = "HousesId not found"
    }
}
