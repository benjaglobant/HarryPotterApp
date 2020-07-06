package com.globant.di

import androidx.room.Room
import com.globant.data.database.HarryPotterRoomDataBaseImpl
import com.globant.data.service.SpellsServiceImpl
import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetSpellsFromAPIUseCase
import com.globant.domain.usecase.GetSpellsFromDataBaseUseCase
import com.globant.domain.usecase.UpdateSpellsDataBaseUseCase
import com.globant.domain.usecase.implementation.GetSpellsFromAPIUseCaseImpl
import com.globant.domain.usecase.implementation.GetSpellsFromDataBaseUseCaseImpl
import com.globant.domain.usecase.implementation.UpdateSpellsDataBaseUseCaseImpl
import org.koin.dsl.module

val serviceModule = module {
    single<SpellsService> { SpellsServiceImpl() }
}

val databaseModule = module {
    single<HarryPotterRoomDataBase> { Room.databaseBuilder(get(), HarryPotterRoomDataBaseImpl::class.java, DATA_BASE_NAME).build() }
    single { get<HarryPotterRoomDataBaseImpl>().harryPotterDao() }
}

val useCaseModule = module {
    single<GetSpellsFromAPIUseCase> { GetSpellsFromAPIUseCaseImpl(get()) }
    single<GetSpellsFromDataBaseUseCase> { GetSpellsFromDataBaseUseCaseImpl(get()) }
    single<UpdateSpellsDataBaseUseCase> { UpdateSpellsDataBaseUseCaseImpl(get()) }
}

private const val DATA_BASE_NAME = "HarryPotterDatabase"
