package com.globant.harrypotterapp.viewmodel.contract

import androidx.lifecycle.LiveData
import com.globant.harrypotterapp.viewmodel.SplashState
import kotlinx.coroutines.Job

interface SplashContract {
    interface ViewModel {
        fun startTimer(): Job
        fun getSplashStateLiveData(): LiveData<SplashState>
    }
}
