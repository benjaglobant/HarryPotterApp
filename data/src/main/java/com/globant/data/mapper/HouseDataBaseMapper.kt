package com.globant.data.mapper

import com.globant.data.entity.HouseRoom
import com.globant.domain.entity.House

class HouseDataBaseMapper : BaseMapper<HouseRoom, House> {
    override fun transform(type: HouseRoom): House = type.run {
        House(
            this.id,
            this.name
        )
    }

    fun transformToData(house: House): HouseRoom = house.run {
        HouseRoom(
            this.name,
            this.id
        )
    }

    fun transformToListOfHouse(housesRoom: List<HouseRoom>): List<House> = housesRoom.map { transform(it) }
}
