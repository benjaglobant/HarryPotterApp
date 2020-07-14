package com.globant.domain.usecase.implementation

import com.globant.domain.entity.HouseId
import com.globant.domain.service.HousesIdService
import com.globant.domain.usecase.GetHousesIdsUseCase
import com.globant.domain.util.Result

class GetHousesIdsUseCaseImpl(private val housesIdService: HousesIdService) : GetHousesIdsUseCase {
    override fun invoke(): Result<List<HouseId>> =
        housesIdService.getHousesId()
}
