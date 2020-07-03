package com.globant.harrypotterapp.spellstest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.entity.Spell
import com.globant.domain.usecase.GetSpellsFromAPIUseCase
import com.globant.domain.util.Result
import com.globant.harrypotterapp.util.Data
import com.globant.harrypotterapp.util.Status
import com.globant.harrypotterapp.viewmodel.SpellsViewModel
import com.globant.harrypotterapp.viewmodel.contract.SpellsContract
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
class SpellsViewModelTest {
    @ObsoleteCoroutinesApi
    private var mainThreadSurrogate = newSingleThreadContext(UI_THREAD)

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var spellsViewModel: SpellsContract.ViewModel
    private var mockedGetSpellsFromApiUseCase: GetSpellsFromAPIUseCase = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        spellsViewModel = SpellsViewModel(mockedGetSpellsFromApiUseCase)
    }

    @After
    fun after() {
        mainThreadSurrogate.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetchSpells returns a failure result`() {
        val spellsLiveData = spellsViewModel.getSpellsLiveData().testObserver()
        val failureResult: Result.Failure = mock()
        val exception: Exception = mock()
        val responseList = listOf(
            Data(status = Status.LOADING, data = null, error = null),
            Data(status = Status.ERROR, data = null, error = exception)
        )
        whenever(mockedGetSpellsFromApiUseCase.invoke()).thenReturn(failureResult)
        whenever(failureResult.exception).thenReturn(exception)
        runBlocking {
            spellsViewModel.fetchSpells().join()
        }

        verify(mockedGetSpellsFromApiUseCase).invoke()

        assertEquals(responseList[ZERO].status, spellsLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(responseList[ONE].status, spellsLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(responseList[ONE].error, spellsLiveData.observedValues[ONE]?.peekContent()?.error)
    }

    @Test
    fun `when fetchSpells returns a success result`() {
        val spellsLiveData = spellsViewModel.getSpellsLiveData().testObserver()
        val successResult: Result.Success<List<Spell>> = mock()
        val listOfSpells: List<Spell> = mock()
        val responseList = listOf(
            Data(status = Status.LOADING, data = null, error = null),
            Data(status = Status.SUCCESS, data = listOfSpells, error = null)
        )

        whenever(mockedGetSpellsFromApiUseCase.invoke()).thenReturn(successResult)
        whenever(successResult.data).thenReturn(listOfSpells)
        runBlocking {
            spellsViewModel.fetchSpells().join()
        }

        verify(mockedGetSpellsFromApiUseCase).invoke()

        assertEquals(responseList[ZERO].status, spellsLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(responseList[ONE].status, spellsLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(responseList[ONE].data, spellsLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    companion object {
        private const val UI_THREAD = "UI thread"
        private const val ZERO = 0
        private const val ONE = 1
    }
}
