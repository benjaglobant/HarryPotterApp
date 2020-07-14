package com.globant.data.service

import com.globant.data.mapper.HousesIdMapper
import com.globant.data.service.api.HarryPotterApi
import com.globant.domain.entity.HouseId
import com.globant.domain.service.HousesIdService
import com.globant.domain.util.Result

class HousesIdServiceImpl : HousesIdService {

    private val api = ServiceGenerator()
    private val mapper = HousesIdMapper()

    override fun getHousesId(): Result<List<HouseId>> {
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
