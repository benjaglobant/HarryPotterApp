package com.globant.data.mapper

import com.globant.data.service.response.HouseResponse
import com.globant.domain.entity.House

class HouseMapper : BaseMapper<HouseResponse, House> {
    override fun transform(type: HouseResponse): House = type.run {
        House(
            this.id,
            this.name
        )
    }

    fun transformListOfHouse(housesResponse: List<HouseResponse>): List<House> =
        housesResponse.map { transform(it) }
}
