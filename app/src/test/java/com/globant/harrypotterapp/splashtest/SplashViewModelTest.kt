package com.globant.harrypotterapp.splashtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.harrypotterapp.viewmodel.SplashState
import com.globant.harrypotterapp.viewmodel.SplashViewModel
import com.globant.harrypotterapp.viewmodel.contract.SplashContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.globant.harrypotterapp.testObserver
import com.globant.harrypotterapp.util.Constants.FIRST_RESPONSE
import com.globant.harrypotterapp.util.Constants.SECOND_RESPONSE

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var splashViewModel: SplashContract.ViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        splashViewModel = SplashViewModel()
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `on startTimer called`() {
        val liveDataUnderTest = splashViewModel.getSplashStateLiveData().testObserver()

        runBlockingTest(testDispatcher) {
            splashViewModel.startTimer().join()
        }

        assertEquals(SplashState.START, liveDataUnderTest.observedValues[FIRST_RESPONSE])
        assertEquals(SplashState.FINISH, liveDataUnderTest.observedValues[SECOND_RESPONSE])
    }
}
