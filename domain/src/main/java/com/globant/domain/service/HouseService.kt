package com.globant.domain.service

import com.globant.domain.entity.House
import com.globant.domain.util.Result

interface HouseService {
    fun getHousesId(): Result<List<House>>
}
