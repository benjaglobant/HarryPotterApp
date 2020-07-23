package com.globant.harrypotterapp.characterstest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.entity.Character
import com.globant.domain.service.CharactersService
import com.globant.domain.usecase.GetCharactersUseCase
import com.globant.domain.usecase.implementation.GetCharactersUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.viewmodel.CharactersData
import com.globant.harrypotterapp.viewmodel.CharactersStatus
import com.globant.harrypotterapp.viewmodel.CharactersViewModel
import com.globant.harrypotterapp.viewmodel.contract.CharactersContract
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
class CharactersViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var charactersViewModel: CharactersContract.ViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private val mockedCharactersService: CharactersService = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCharactersUseCase = GetCharactersUseCaseImpl(mockedCharactersService)
        charactersViewModel = CharactersViewModel(getCharactersUseCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when fetchCharacters return success result`() {
        val charactersLiveData = charactersViewModel.getCharactersLiveData().testObserver()
        val listOfCharacters: List<Character> = mock()
        val successResponseList = listOf(
            CharactersData(status = CharactersStatus.LOADING_CHARACTERS),
            CharactersData(status = CharactersStatus.SUCCESS_CHARACTERS, data = listOfCharacters)
        )

        whenever(mockedCharactersService.getCharactersByHouse(GRYFFINDOR)).thenReturn(Result.Success(listOfCharacters))

        runBlocking {
            charactersViewModel.fetchCharacters(GRYFFINDOR).join()
        }

        verify(mockedCharactersService).getCharactersByHouse(GRYFFINDOR)

        assertEquals(successResponseList[ZERO].status, charactersLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].status, charactersLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(successResponseList[ONE].data, charactersLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    @Test
    fun `when fetchCharacters return failure result`() {
        val charactersLiveData = charactersViewModel.getCharactersLiveData().testObserver()
        val exception: Exception = mock()
        val failureResponseList = listOf(
            CharactersData(status = CharactersStatus.LOADING_CHARACTERS),
            CharactersData(status = CharactersStatus.ERROR_CHARACTERS, data = null, error = exception)
        )

        whenever(mockedCharactersService.getCharactersByHouse(GRYFFINDOR)).thenReturn(Result.Failure(exception))

        runBlocking {
            charactersViewModel.fetchCharacters(GRYFFINDOR).join()
        }

        verify(mockedCharactersService).getCharactersByHouse(GRYFFINDOR)

        assertEquals(failureResponseList[ZERO].status, charactersLiveData.observedValues[ZERO]?.peekContent()?.status)
        assertEquals(failureResponseList[ONE].status, charactersLiveData.observedValues[ONE]?.peekContent()?.status)
        assertEquals(failureResponseList[ONE].error, charactersLiveData.observedValues[ONE]?.peekContent()?.error)
    }

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
        private const val GRYFFINDOR = "Gryffindor"
    }
}
