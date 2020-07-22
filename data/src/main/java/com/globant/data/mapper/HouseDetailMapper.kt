package com.globant.data.mapper

import com.globant.data.service.response.HouseDetailResponse
import com.globant.domain.entity.HouseDetail

class HouseDetailMapper : BaseMapper<HouseDetailResponse, HouseDetail> {
    override fun transform(type: HouseDetailResponse): HouseDetail = type.run {
        HouseDetail(
            this.name,
            this.mascot,
            this.director,
            this.houseGhost,
            this.founder
        )
    }

    fun transformToListOfHouseDetail(list: List<HouseDetailResponse>): List<HouseDetail> =
        list.map { transform(it) }
}
