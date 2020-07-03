package com.globant.harrypotterapp

import android.app.Application
import com.globant.di.serviceModule
import com.globant.di.useCaseModule
import com.globant.harrypotterapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(viewModelModule, useCaseModule, serviceModule))
        }
    }
}
