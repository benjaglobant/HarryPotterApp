package com.globant.harrypotterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.harrypotterapp.viewmodel.contract.SplashContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel(), SplashContract.ViewModel {

    private val splashStateMutableLiveData = MutableLiveData<SplashState>()
    override fun getSplashStateLiveData(): LiveData<SplashState> = splashStateMutableLiveData

    override fun startTimer() = viewModelScope.launch {
        delay(SPLASH_TIME)
        splashStateMutableLiveData.postValue(SplashState.START)
        delay(SPLASH_TIME)
        splashStateMutableLiveData.postValue(SplashState.FINISH)
    }

    companion object {
        private const val SPLASH_TIME = 2000L
    }
}

enum class SplashState {
    START,
    FINISH
}
