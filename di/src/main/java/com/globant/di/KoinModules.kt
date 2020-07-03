package com.globant.di

import com.globant.data.service.SpellsServiceImpl
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetSpellsFromAPIUseCase
import com.globant.domain.usecase.implementation.GetSpellsFromAPIUseCaseImpl
import org.koin.dsl.module

val serviceModule = module {
    single<SpellsService> { SpellsServiceImpl() }
}

val useCaseModule = module {
    single<GetSpellsFromAPIUseCase> { GetSpellsFromAPIUseCaseImpl(get()) }
}
