package com.globant.harrypotterapp.di

import com.globant.harrypotterapp.viewmodel.CharacterDetailViewModel
import com.globant.harrypotterapp.viewmodel.CharactersViewModel
import com.globant.harrypotterapp.viewmodel.HouseDetailViewModel
import com.globant.harrypotterapp.viewmodel.MainViewModel
import com.globant.harrypotterapp.viewmodel.SpellsViewModel
import com.globant.harrypotterapp.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { SpellsViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { HouseDetailViewModel(get()) }
    viewModel { CharactersViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
}
