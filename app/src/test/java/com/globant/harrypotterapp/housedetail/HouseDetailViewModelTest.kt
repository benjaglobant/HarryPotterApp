package com.globant.harrypotterapp.housedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.House
import com.globant.domain.entity.HouseDetail
import com.globant.domain.service.HouseDetailService
import com.globant.domain.usecase.GetHouseDetailByIdUseCase
import com.globant.domain.usecase.implementation.GetHouseDetailByIdUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.viewmodel.HouseDetailData
import com.globant.harrypotterapp.viewmodel.HouseDetailStatus
import com.globant.harrypotterapp.viewmodel.HouseDetailViewModel
import com.globant.harrypotterapp.viewmodel.contract.HouseDetailContract
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
class HouseDetailViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var houseDetailViewModel: HouseDetailContract.ViewModel
    private lateinit var getHouseDetailByIdUseCase: GetHouseDetailByIdUseCase
    private val mockedHouseDetailService: HouseDetailService = mock()
    private val mockedDataBase: HarryPotterDataBase = mock()
    private val listOfHouseDetail: List<HouseDetail> = mock()
    private val exception: Exception = mock()
    private val successResponseList = listOf(
        HouseDetailData(status = HouseDetailStatus.LOADING_HOUSE_DETAIL),
        HouseDetailData(status = HouseDetailStatus.SUCCESS_HOUSE_DETAIL, data = listOfHouseDetail)
    )
    private val failureResponseList = listOf(
        HouseDetailData(status = HouseDetailStatus.LOADING_HOUSE_DETAIL),
        HouseDetailData(status = HouseDetailStatus.ERROR_HOUSE_DETAIL, data = null, error = exception)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getHouseDetailByIdUseCase = GetHouseDetailByIdUseCaseImpl(mockedHouseDetailService, mockedDataBase)
        houseDetailViewModel = HouseDetailViewModel(getHouseDetailByIdUseCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when getHouseDetail return success result`() {
        val houseDetailLiveData = houseDetailViewModel.getHouseDetailLiveData().testObserver()

        whenever(mockedDataBase.getHouseByName(GRYFFINDOR)).thenReturn(Result.Success(data = listOf(House(GRYFFINDOR_ID, GRYFFINDOR))))
        whenever(mockedHouseDetailService.getHouseDetailById(GRYFFINDOR_ID)).thenReturn(Result.Success(data = listOfHouseDetail))

        runBlocking {
            houseDetailViewModel.fetchHouseDetail(GRYFFINDOR).join()
        }

        verify(mockedDataBase).getHouseByName(GRYFFINDOR)
        verify(mockedHouseDetailService).getHouseDetailById(GRYFFINDOR_ID)

        assertEquals(successResponseList[ZERO].status, houseDetailLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].status, houseDetailLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].data, houseDetailLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    @Test
    fun `when getHouseDetail return failure result`() {
        val houseDetailLiveData = houseDetailViewModel.getHouseDetailLiveData().testObserver()

        whenever(mockedDataBase.getHouseByName(WRONG_HOUSE_NAME)).thenReturn(Result.Failure(exception = exception))

        runBlocking {
            houseDetailViewModel.fetchHouseDetail(WRONG_HOUSE_NAME).join()
        }

        verify(mockedDataBase).getHouseByName(WRONG_HOUSE_NAME)

        assertEquals(failureResponseList[ZERO].status, houseDetailLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(failureResponseList[ONE].status, houseDetailLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(failureResponseList[ONE].error, houseDetailLiveData.observedValues[ONE]?.peekContent()?.error)
    }

    @Test
    fun `on getting houseDetail from API returns failure in result, then gets data from database`() {
        val houseDetailLiveData = houseDetailViewModel.getHouseDetailLiveData().testObserver()

        whenever(mockedDataBase.getHouseByName(GRYFFINDOR)).thenReturn(Result.Success(data = listOf(House(GRYFFINDOR_ID, GRYFFINDOR))))
        whenever(mockedHouseDetailService.getHouseDetailById(GRYFFINDOR_ID)).thenReturn(Result.Failure(exception = exception))
        whenever(mockedDataBase.getHouseDetailByName(GRYFFINDOR)).thenReturn(Result.Success(data = listOfHouseDetail))

        runBlocking {
            houseDetailViewModel.fetchHouseDetail(GRYFFINDOR).join()
        }

        verify(mockedHouseDetailService).getHouseDetailById(GRYFFINDOR_ID)
        verify(mockedDataBase).getHouseDetailByName(GRYFFINDOR)

        assertEquals(successResponseList[ZERO].status, houseDetailLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].status, houseDetailLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].data, houseDetailLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    @Test
    fun `on getting houseDetail from API returns failure in result, then database returns failure too`() {
        val spellsLiveData = houseDetailViewModel.getHouseDetailLiveData().testObserver()
        val failureResult = Result.Failure(exception = exception)

        whenever(mockedDataBase.getHouseByName(GRYFFINDOR)).thenReturn(Result.Success(data = listOf(House(GRYFFINDOR_ID, GRYFFINDOR))))
        whenever(mockedHouseDetailService.getHouseDetailById(GRYFFINDOR_ID)).thenReturn(failureResult)
        whenever(mockedDataBase.getHouseDetailByName(GRYFFINDOR)).thenReturn(failureResult)

        runBlocking {
            houseDetailViewModel.fetchHouseDetail(GRYFFINDOR).join()
        }

        verify(mockedHouseDetailService).getHouseDetailById(GRYFFINDOR_ID)
        verify(mockedDataBase).getHouseDetailByName(GRYFFINDOR)

        assertEquals(failureResponseList[ZERO].status, spellsLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(failureResponseList[ONE].status, spellsLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(failureResponseList[ONE].error, spellsLiveData.observedValues[ONE]?.peekContent()?.error)
    }

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
        private const val GRYFFINDOR = "Gryffindor"
        private const val WRONG_HOUSE_NAME = "Griffindor"
        private const val GRYFFINDOR_ID = "5a05e2b252f721a3cf2ea33f"
    }
}
