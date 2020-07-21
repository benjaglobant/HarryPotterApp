package com.globant.harrypotterapp.maintest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.entity.House
import com.globant.domain.service.HouseService
import com.globant.domain.usecase.GetHousesUseCase
import com.globant.domain.usecase.implementation.GetHousesUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.viewmodel.MainData
import com.globant.harrypotterapp.viewmodel.MainStatus
import com.globant.harrypotterapp.viewmodel.MainViewModel
import com.globant.harrypotterapp.viewmodel.contract.MainContract
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainContract.ViewModel
    private lateinit var getHousesUseCase: GetHousesUseCase
    private val mockedHouseService: HouseService = mock()
    private val mockedDataBase: HarryPotterRoomDataBase = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getHousesUseCase = GetHousesUseCaseImpl(mockedHouseService, mockedDataBase)
        mainViewModel = MainViewModel(getHousesUseCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when fetchHouses returns a success result`() {
        val housesLiveData = mainViewModel.getHousesLiveData().testObserver()
        val successResult: Result.Success<List<House>> = mock()
        val listOfHouses: List<House> = mock()
        val successResponseList = listOf(
            MainData(status = MainStatus.LOADING_MAIN),
            MainData(status = MainStatus.SUCCESS_MAIN, data = listOfHouses)
        )

        whenever(mockedHouseService.getHouses()).thenReturn(successResult)
        whenever(successResult.data).thenReturn(listOfHouses)
        runBlocking {
            mainViewModel.fetchHouses().join()
        }

        verify(mockedHouseService).getHouses()
        verify(mockedDataBase).updateHouses(listOfHouses)

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
            MainData(status = MainStatus.LOADING_MAIN),
            MainData(status = MainStatus.ERROR_MAIN, data = null, error = exception)
        )

        whenever(mockedHouseService.getHouses()).thenReturn(failureResult)
        whenever(failureResult.exception).thenReturn(exception)
        whenever(mockedDataBase.getHouses()).thenReturn(failureResult)

        runBlocking {
            mainViewModel.fetchHouses().join()
        }

        verify(mockedHouseService).getHouses()
        verify(mockedDataBase).getHouses()

        assertEquals(errorResponseList[ZERO].status, housesLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(errorResponseList[ONE].status, housesLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(errorResponseList[ONE].data, housesLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
    }
}
