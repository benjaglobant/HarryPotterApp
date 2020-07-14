package com.globant.domain.service

import com.globant.domain.entity.HouseId
import com.globant.domain.util.Result

interface HousesIdService {
    fun getHousesId(): Result<List<HouseId>>
}
