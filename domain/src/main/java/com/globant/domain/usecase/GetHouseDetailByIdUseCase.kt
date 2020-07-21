package com.globant.domain.usecase

import com.globant.domain.entity.HouseDetail
import com.globant.domain.util.Result

interface GetHouseDetailByIdUseCase {
    operator fun invoke(houseName: String): Result<List<HouseDetail>>
}
