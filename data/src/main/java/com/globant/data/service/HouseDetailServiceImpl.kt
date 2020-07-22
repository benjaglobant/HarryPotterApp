package com.globant.data.service

import com.globant.data.mapper.HouseDetailMapper
import com.globant.data.service.api.HarryPotterApi
import com.globant.domain.entity.HouseDetail
import com.globant.domain.service.HouseDetailService
import com.globant.domain.util.Result

class HouseDetailServiceImpl : HouseDetailService {

    private val api = ServiceGenerator()
    private val mapper = HouseDetailMapper()

    override fun getHouseDetailById(houseId: String): Result<List<HouseDetail>> {
        try {
            val callResponse = api.createService(HarryPotterApi::class.java).getHouseDetail(houseId)
            val response = callResponse.execute()
            if (response.isSuccessful)
                response.body()?.let {
                    mapper.transformToListOfHouseDetail(it)
                }?.let {
                    return Result.Success(it)
                }
        } catch (e: Exception) {
            return Result.Failure(Exception(NOT_FOUND))
        }
        return Result.Failure(Exception(NOT_FOUND))
    }

    companion object {
        private const val NOT_FOUND = "House detail not found"
    }
}
