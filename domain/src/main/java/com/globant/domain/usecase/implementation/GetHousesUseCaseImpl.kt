package com.globant.domain.usecase.implementation

import com.globant.domain.entity.House
import com.globant.domain.service.HouseService
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.util.Result

class GetHousesUseCaseImpl(private val houseService: HouseService) : GetHousesUseCase {
    override operator fun invoke(): Result<List<House>> =
        houseService.getHousesId()
}
