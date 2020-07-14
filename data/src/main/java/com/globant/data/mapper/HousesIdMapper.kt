package com.globant.data.mapper

import com.globant.data.service.response.HouseIdResponse
import com.globant.domain.entity.HouseId

class HousesIdMapper : BaseMapper<HouseIdResponse, HouseId> {
    override fun transform(type: HouseIdResponse): HouseId = type.run {
        HouseId(
            this._id,
            this.name
        )
    }

    fun transformListOfHouseId(housesIdResponse: List<HouseIdResponse>): List<HouseId> =
        housesIdResponse.map { transform(it) }
}
