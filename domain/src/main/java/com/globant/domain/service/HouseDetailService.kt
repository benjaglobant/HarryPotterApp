package com.globant.domain.service

import com.globant.domain.entity.HouseDetail
import com.globant.domain.util.Result

interface HouseDetailService {
    fun getHouseDetailById(houseId: String): Result<List<HouseDetail>>
}
