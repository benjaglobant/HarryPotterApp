package com.globant.data.mapper

import com.globant.data.service.response.HouseResponse
import com.globant.domain.entity.House

class HouseMapper : BaseMapper<HouseResponse, House> {
    override fun transform(type: HouseResponse): House = type.run {
        House(
            this._id,
            this.name
        )
    }

    fun transformListOfHouseId(housesResponse: List<HouseResponse>): List<House> =
        housesResponse.map { transform(it) }
}
