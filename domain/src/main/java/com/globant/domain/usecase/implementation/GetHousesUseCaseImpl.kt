package com.globant.domain.usecase.implementation

import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.entity.House
import com.globant.domain.service.HouseService
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.util.Result

class GetHousesUseCaseImpl(
    private val houseService: HouseService,
    private val database: HarryPotterRoomDataBase
) : GetHousesUseCase {
    override operator fun invoke(): Result<List<House>> =
        when (val result = houseService.getHouses()) {
            is Result.Success -> {
                database.updateHouses(result.data)
                result
            }
            is Result.Failure -> {
                result
            }
        }
}
