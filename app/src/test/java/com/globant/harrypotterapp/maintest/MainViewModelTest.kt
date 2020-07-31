package com.globant.harrypotterapp.maintest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterDataBase
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
import com.globant.harrypotterapp.testObserver
import com.globant.harrypotterapp.util.Constants.FIRST_RESPONSE
import com.globant.harrypotterapp.util.Constants.GRYFFINDOR
import com.globant.harrypotterapp.util.Constants.SECOND_RESPONSE

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainContract.ViewModel
    private lateinit var getHousesUseCase: GetHousesUseCase
    private val mockedHouseService: HouseService = mock()
    private val mockedDataBase: HarryPotterDataBase = mock()

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
            MainData(status = MainStatus.SUCCESS_MAIN)
        )

        whenever(mockedHouseService.getHouses()).thenReturn(successResult)
        whenever(successResult.data).thenReturn(listOfHouses)
        runBlocking {
            mainViewModel.fetchHouses().join()
        }

        verify(mockedHouseService).getHouses()
        verify(mockedDataBase).updateHouses(listOfHouses)

        assertEquals(successResponseList[FIRST_RESPONSE].status, housesLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].status, housesLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
    }

    @Test
    fun `when fetchHouses returns a failure result`() {
        val housesLiveData = mainViewModel.getHousesLiveData().testObserver()
        val failureResult: Result.Failure = mock()
        val exception: Exception = mock()
        val errorResponseList = listOf(
            MainData(status = MainStatus.LOADING_MAIN),
            MainData(status = MainStatus.ERROR_MAIN, error = exception)
        )

        whenever(mockedHouseService.getHouses()).thenReturn(failureResult)
        whenever(failureResult.exception).thenReturn(exception)

        runBlocking {
            mainViewModel.fetchHouses().join()
        }

        verify(mockedHouseService).getHouses()

        assertEquals(errorResponseList[FIRST_RESPONSE].status, housesLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(errorResponseList[SECOND_RESPONSE].status, housesLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(errorResponseList[SECOND_RESPONSE].error, housesLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.error)
    }

    @Test
    fun `when goToSpells is clicked then post a new state in live data`() {
        val housesLiveData = mainViewModel.getHousesLiveData().testObserver()

        mainViewModel.goToSpells()

        assertEquals(MainData(status = MainStatus.GO_SPELLS).status, housesLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
    }

    @Test
    fun `when goToHouse is clicked then post a new state in live data`() {
        val housesLiveData = mainViewModel.getHousesLiveData().testObserver()
        val mainStatus = MainData(status = MainStatus.GO_HOUSE, houseName = GRYFFINDOR)

        mainViewModel.goToHouse(GRYFFINDOR)

        assertEquals(mainStatus.status, housesLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(mainStatus.houseName, housesLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.houseName)
    }
}
