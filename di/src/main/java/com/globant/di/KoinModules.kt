package com.globant.di

import androidx.room.Room
import com.globant.data.database.HarryPotterRoomDataBaseImpl
import com.globant.data.service.HouseServiceImpl
import com.globant.data.service.SpellsServiceImpl
import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.service.HouseService
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.usecase.GetSpellsUseCase
import com.globant.domain.usecase.implementation.GetHousesUseCaseImpl
import com.globant.domain.usecase.implementation.GetSpellsUseCaseImpl
import org.koin.dsl.module

val serviceModule = module {
    single<SpellsService> { SpellsServiceImpl() }
    single<HouseService> { HouseServiceImpl() }
}

val databaseModule = module {
    single<HarryPotterRoomDataBase> { Room.databaseBuilder(get(), HarryPotterRoomDataBaseImpl::class.java, DATA_BASE_NAME).build() }
    single { get<HarryPotterRoomDataBaseImpl>().harryPotterDao() }
}

val useCaseModule = module {
    single<GetSpellsUseCase> { GetSpellsUseCaseImpl(get(), get()) }
    single<GetHousesUseCase> { GetHousesUseCaseImpl(get()) }
}

private const val DATA_BASE_NAME = "HarryPotterDatabase"
