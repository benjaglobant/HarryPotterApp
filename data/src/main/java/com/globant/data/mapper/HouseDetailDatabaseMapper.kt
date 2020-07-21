package com.globant.data.mapper

import com.globant.data.entity.HouseDetailEntity
import com.globant.domain.entity.HouseDetail

class HouseDetailDatabaseMapper : BaseMapper<HouseDetailEntity, HouseDetail> {
    override fun transform(type: HouseDetailEntity): HouseDetail = type.run {
        HouseDetail(
            this.name,
            this.mascot,
            this.director,
            this.ghost,
            this.founder
        )
    }

    fun transformToData(type: HouseDetail): HouseDetailEntity = type.run {
        HouseDetailEntity(
            this.name,
            this.mascot,
            this.headOfHouse,
            this.houseGhost,
            this.founder
        )
    }

    fun transformToListOfHouseDetail(housesDetail: List<HouseDetailEntity>): List<HouseDetail> =
        housesDetail.map { transform(it) }
}
