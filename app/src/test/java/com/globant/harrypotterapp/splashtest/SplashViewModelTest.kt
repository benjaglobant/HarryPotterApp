package com.globant.harrypotterapp.splashtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.harrypotterapp.contract.SplashContract
import com.globant.harrypotterapp.viewmodel.SplashState
import com.globant.harrypotterapp.viewmodel.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import test.com.globant.harrypotterapp.testObserver

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {
    @ObsoleteCoroutinesApi
    private var mainThreadSurrogate = newSingleThreadContext(UI_THREAD)

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var splashViewModel: SplashContract.ViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        splashViewModel = SplashViewModel()
    }

    @After
    fun after() {
        mainThreadSurrogate.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `on startTimer called`() {
        val liveDataUnderTest = splashViewModel.getSplashStateLiveData().testObserver()

        runBlocking {
            splashViewModel.startTimer().join()
        }

        assertEquals(SplashState.START, liveDataUnderTest.observedValues[ZERO])
        assertEquals(SplashState.FINISH, liveDataUnderTest.observedValues[ONE])
    }

    companion object {
        private const val UI_THREAD = "UI thread"
        private const val ZERO = 0
        private const val ONE = 1
    }
}
