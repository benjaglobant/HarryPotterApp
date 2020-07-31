package com.globant.harrypotterapp.spellstest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.Spell
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetSpellsUseCase
import com.globant.domain.usecase.implementation.GetSpellsUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.viewmodel.SpellData
import com.globant.harrypotterapp.viewmodel.SpellStatus
import com.globant.harrypotterapp.viewmodel.SpellsViewModel
import com.globant.harrypotterapp.viewmodel.contract.SpellsContract
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
import com.globant.harrypotterapp.util.Constants.SECOND_RESPONSE

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SpellsViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var spellsViewModel: SpellsContract.ViewModel
    private lateinit var getSpellsUseCase: GetSpellsUseCase
    private val mockedDatabase: HarryPotterDataBase = mock()
    private val mockedSpellsService: SpellsService = mock()
    private val successResult: Result.Success<List<Spell>> = mock()
    private val failureResult: Result.Failure = mock()
    private val exception: Exception = mock()
    private val listOfSpells: List<Spell> = mock()
    private val successResponseList = listOf(
        SpellData(status = SpellStatus.LOADING_SPELLS),
        SpellData(status = SpellStatus.SUCCESS_SPELLS, data = listOfSpells)
    )
    private val errorResponseList = listOf(
        SpellData(status = SpellStatus.LOADING_SPELLS),
        SpellData(status = SpellStatus.ERROR_SPELLS, data = null, error = exception)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getSpellsUseCase = GetSpellsUseCaseImpl(mockedSpellsService, mockedDatabase)
        spellsViewModel = SpellsViewModel(getSpellsUseCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when fetchSpells returns a success result`() {
        val spellsLiveData = spellsViewModel.getSpellsLiveData().testObserver()

        whenever(mockedSpellsService.getSpells()).thenReturn(successResult)
        whenever(successResult.data).thenReturn(listOfSpells)
        runBlocking {
            spellsViewModel.fetchSpells().join()
        }

        verify(mockedSpellsService).getSpells()
        verify(mockedDatabase).updateSpells(listOfSpells)

        assertEquals(successResponseList[FIRST_RESPONSE].status, spellsLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].status, spellsLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].data, spellsLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.data)
    }

    @Test
    fun `trying to get data twice but get null in the second time`() {
        val spellsLiveData = spellsViewModel.getSpellsLiveData().testObserver()

        whenever(mockedSpellsService.getSpells()).thenReturn(successResult)
        whenever(successResult.data).thenReturn(listOfSpells)

        runBlocking {
            spellsViewModel.fetchSpells().join()
        }

        verify(mockedSpellsService).getSpells()

        val dataFirstTime = spellsLiveData.observedValues[SECOND_RESPONSE]?.getContentIfNotHandled()
        assertEquals(successResponseList[SECOND_RESPONSE].status, dataFirstTime?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].data, dataFirstTime?.data)

        val dataSecondTime = spellsLiveData.observedValues[SECOND_RESPONSE]?.getContentIfNotHandled()
        assertEquals(null, dataSecondTime?.data)
        assertEquals(null, dataSecondTime?.status)
    }

    @Test
    fun `on getting spells from API returns failure in result, then gets data from database`() {
        val spellsLiveData = spellsViewModel.getSpellsLiveData().testObserver()

        whenever(mockedSpellsService.getSpells()).thenReturn(failureResult)
        whenever(mockedDatabase.getSpellsFromDataBase()).thenReturn(successResult)
        whenever(successResult.data).thenReturn(listOfSpells)

        runBlocking {
            spellsViewModel.fetchSpells().join()
        }

        verify(mockedSpellsService).getSpells()
        verify(mockedDatabase).getSpellsFromDataBase()

        assertEquals(successResponseList[FIRST_RESPONSE].status, spellsLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].status, spellsLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].data, spellsLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.data)
    }

    @Test
    fun `on getting spells from API returns failure in result, then database returns failure too`() {
        val spellsLiveData = spellsViewModel.getSpellsLiveData().testObserver()

        whenever(mockedSpellsService.getSpells()).thenReturn(failureResult)
        whenever(mockedDatabase.getSpellsFromDataBase()).thenReturn(failureResult)
        whenever(failureResult.exception).thenReturn(exception)

        runBlocking {
            spellsViewModel.fetchSpells().join()
        }

        verify(mockedSpellsService).getSpells()
        verify(mockedDatabase).getSpellsFromDataBase()

        assertEquals(errorResponseList[FIRST_RESPONSE].status, spellsLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(errorResponseList[SECOND_RESPONSE].status, spellsLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(errorResponseList[SECOND_RESPONSE].error, spellsLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.error)
    }
}
