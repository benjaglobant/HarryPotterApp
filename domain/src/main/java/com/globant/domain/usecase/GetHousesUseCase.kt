package com.globant.domain.usecase

import com.globant.domain.entity.House
import com.globant.domain.util.Result
import org.koin.core.KoinComponent

interface GetHousesUseCase : KoinComponent {
    operator fun invoke(): Result<List<House>>
}
