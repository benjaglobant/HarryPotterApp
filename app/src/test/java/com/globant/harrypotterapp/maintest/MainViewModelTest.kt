package com.globant.harrypotterapp.maintest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.entity.House
import com.globant.domain.service.HouseService
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.usecase.implementation.GetHousesUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Status
import com.globant.harrypotterapp.viewmodel.MainViewModel
import com.globant.harrypotterapp.viewmodel.contract.MainContract
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @ObsoleteCoroutinesApi
    private var mainThreadSurrogate = newSingleThreadContext(TEST_THREAD)

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainContract.ViewModel
    private lateinit var getHousesUseCase: GetHousesUseCase
    private val mockedHouseService: HouseService = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        getHousesUseCase = GetHousesUseCaseImpl(mockedHouseService)
        mainViewModel = MainViewModel(getHousesUseCase)
    }

    @After
    fun after() {
        mainThreadSurrogate.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetchHouses returns a success result`() {
        val housesLiveData = mainViewModel.getHousesLiveData().testObserver()
        val successResult: Result.Success<List<House>> = mock()
        val listOfHouses: List<House> = mock()
        val successResponseList = listOf(
            Data(status = Status.LOADING),
            Data(status = Status.SUCCESS, data = listOfHouses)
        )

        whenever(mockedHouseService.getHouses()).thenReturn(successResult)
        whenever(successResult.data).thenReturn(listOfHouses)
        runBlocking {
            mainViewModel.fetchHouses().join()
        }

        verify(mockedHouseService).getHouses()

        assertEquals(successResponseList[ZERO].status, housesLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].status, housesLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].data, housesLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    @Test
    fun `when fetchHouses returns a failure result`() {
        val housesLiveData = mainViewModel.getHousesLiveData().testObserver()
        val failureResult: Result.Failure = mock()
        val exception: Exception = mock()
        val errorResponseList = listOf(
            Data(status = Status.LOADING),
            Data(status = Status.ERROR, data = null, error = exception)
        )

        whenever(mockedHouseService.getHouses()).thenReturn(failureResult)
        whenever(failureResult.exception).thenReturn(exception)
        runBlocking {
            mainViewModel.fetchHouses().join()
        }

        verify(mockedHouseService).getHouses()

        assertEquals(errorResponseList[ZERO].status, housesLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(errorResponseList[ONE].status, housesLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(errorResponseList[ONE].data, housesLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    companion object {
        private const val TEST_THREAD = "testThread"
        private const val ZERO = 0
        private const val ONE = 1
    }
}
