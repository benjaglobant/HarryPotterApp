package com.globant.domain.usecase

import com.globant.domain.entity.HouseId
import com.globant.domain.util.Result

interface GetHousesIdsUseCase {
    fun invoke(): Result<List<HouseId>>
}
