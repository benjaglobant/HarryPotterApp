package com.globant.domain.usecase.implementation

import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.HouseDetail
import com.globant.domain.service.HouseDetailService
import com.globant.domain.usecase.GetHouseDetailByIdUseCase
import com.globant.domain.util.Result

class GetHouseDetailByIdUseCaseImpl(
    private val houseDetailService: HouseDetailService,
    private val database: HarryPotterDataBase
) : GetHouseDetailByIdUseCase {
    override operator fun invoke(houseName: String): Result<List<HouseDetail>> =
        when (val houseResult = database.getHouseByName(houseName)) {
            is Result.Success -> {
                when (val houseDetailResult = houseDetailService.getHouseDetailById(houseResult.data[ZERO].id)) {
                    is Result.Success -> {
                        database.updateHouseDetail(houseDetailResult.data)
                        houseDetailResult
                    }
                    is Result.Failure -> {
                        database.getHouseDetailByName(houseName)
                    }
                }
            }
            is Result.Failure -> houseResult
        }

    companion object {
        private const val ZERO = 0
    }
}
