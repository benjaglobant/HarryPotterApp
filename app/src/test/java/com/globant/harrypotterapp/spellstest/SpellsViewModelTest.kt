package com.globant.harrypotterapp.spellstest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterRoomDataBase
import com.globant.domain.entity.Spell
import com.globant.domain.service.SpellsService
import com.globant.domain.usecase.GetSpellsUseCase
import com.globant.domain.usecase.implementation.GetSpellsUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Status
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
import test.com.globant.harrypotterapp.testObserver

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SpellsViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var spellsViewModel: SpellsContract.ViewModel
    private lateinit var getSpellsUseCase: GetSpellsUseCase
    private val mockedDatabase: HarryPotterRoomDataBase = mock()
    private val mockedSpellsService: SpellsService = mock()
    private val successResult: Result.Success<List<Spell>> = mock()
    private val failureResult: Result.Failure = mock()
    private val exception: Exception = mock()
    private val listOfSpells: List<Spell> = mock()
    private val successResponseList = listOf(
        Data(status = Status.LOADING),
        Data(status = Status.SUCCESS, data = listOfSpells)
    )
    private val errorResponseList = listOf(
        Data(status = Status.LOADING),
        Data(status = Status.ERROR, data = null, error = exception)
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

        assertEquals(successResponseList[ZERO].status, spellsLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].status, spellsLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].data, spellsLiveData.observedValues[ONE]?.peekContent()?.data)
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

        val dataFirstTime = spellsLiveData.observedValues[ONE]?.getContentIfNotHandled()
        assertEquals(successResponseList[ONE].status, dataFirstTime?.status)
        assertEquals(successResponseList[ONE].data, dataFirstTime?.data)

        val dataSecondTime = spellsLiveData.observedValues[ONE]?.getContentIfNotHandled()
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

        assertEquals(successResponseList[ZERO].status, spellsLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].status, spellsLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].data, spellsLiveData.observedValues[ONE]?.peekContent()?.data)
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

        assertEquals(errorResponseList[ZERO].status, spellsLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(errorResponseList[ONE].status, spellsLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(errorResponseList[ONE].error, spellsLiveData.observedValues[ONE]?.peekContent()?.error)
    }

    companion object {
        private const val TEST_THREAD = "testThread"
        private const val ZERO = 0
        private const val ONE = 1
    }
}
