package com.globant.di

import androidx.room.Room
import com.globant.data.database.HarryPotterDataBaseImpl
import com.globant.data.service.CharactersServiceImpl
import com.globant.data.service.HouseDetailServiceImpl
import com.globant.data.service.HouseServiceImpl
import com.globant.data.service.SpellsServiceImpl
import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.service.CharactersService
import com.globant.domain.service.HouseDetailService
import com.globant.domain.service.HouseService
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetCharactersUseCase
import com.globant.domain.usecase.GetHouseDetailByIdUseCase
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.usecase.GetSpellsUseCase
import com.globant.domain.usecase.implementation.GetCharactersUseCaseImpl
import com.globant.domain.usecase.implementation.GetHouseDetailByIdUseCaseImpl
import com.globant.domain.usecase.implementation.GetHousesUseCaseImpl
import com.globant.domain.usecase.implementation.GetSpellsUseCaseImpl
import org.koin.dsl.module

val serviceModule = module {
    single<SpellsService> { SpellsServiceImpl() }
    single<HouseService> { HouseServiceImpl() }
    single<HouseDetailService> { HouseDetailServiceImpl() }
    single<CharactersService> { CharactersServiceImpl() }
}

val databaseModule = module {
    single<HarryPotterDataBase> { Room.databaseBuilder(get(), HarryPotterDataBaseImpl::class.java, DATA_BASE_NAME).build() }
    single { get<HarryPotterDataBaseImpl>().harryPotterDao() }
}

val useCaseModule = module {
    single<GetSpellsUseCase> { GetSpellsUseCaseImpl(get(), get()) }
    single<GetHousesUseCase> { GetHousesUseCaseImpl(get(), get()) }
    single<GetHouseDetailByIdUseCase> { GetHouseDetailByIdUseCaseImpl(get(), get()) }
    single<GetCharactersUseCase> { GetCharactersUseCaseImpl(get(), get()) }
}

private const val DATA_BASE_NAME = "HarryPotterDatabase"
