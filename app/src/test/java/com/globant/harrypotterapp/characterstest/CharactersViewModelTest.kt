package com.globant.harrypotterapp.characterstest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterDataBase
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
import com.globant.harrypotterapp.testObserver
import com.globant.harrypotterapp.util.Constants.FIRST_RESPONSE
import com.globant.harrypotterapp.util.Constants.GRYFFINDOR
import com.globant.harrypotterapp.util.Constants.SECOND_RESPONSE

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var charactersViewModel: CharactersContract.ViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private val mockedCharactersService: CharactersService = mock()
    private val mockedDatabase: HarryPotterDataBase = mock()
    private val listOfCharacters: List<Character> = mock()
    private val exception: Exception = mock()
    private val successResponseList = listOf(
        CharactersData(status = CharactersStatus.LOADING_CHARACTERS),
        CharactersData(status = CharactersStatus.SUCCESS_CHARACTERS, data = listOfCharacters)
    )
    private val failureResponseList: List<CharactersData<List<Character>>> = listOf(
        CharactersData(status = CharactersStatus.LOADING_CHARACTERS),
        CharactersData(status = CharactersStatus.ERROR_CHARACTERS, error = exception)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCharactersUseCase = GetCharactersUseCaseImpl(mockedCharactersService, mockedDatabase)
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

        whenever(mockedCharactersService.getCharactersByHouse(GRYFFINDOR)).thenReturn(Result.Success(listOfCharacters))

        runBlocking {
            charactersViewModel.fetchCharacters(GRYFFINDOR).join()
        }

        verify(mockedCharactersService).getCharactersByHouse(GRYFFINDOR)

        assertEquals(successResponseList[FIRST_RESPONSE].status, charactersLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].status, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].data, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.data)
    }

    @Test
    fun `on getting characters from API returns failure in result, then gets characters from database`() {
        val charactersLiveData = charactersViewModel.getCharactersLiveData().testObserver()

        whenever(mockedCharactersService.getCharactersByHouse(GRYFFINDOR)).thenReturn(Result.Failure(exception = exception))
        whenever(mockedDatabase.getCharactersByHouseName(GRYFFINDOR)).thenReturn(Result.Success(data = listOfCharacters))

        runBlocking {
            charactersViewModel.fetchCharacters(GRYFFINDOR).join()
        }

        verify(mockedCharactersService).getCharactersByHouse(GRYFFINDOR)
        verify(mockedDatabase).getCharactersByHouseName(GRYFFINDOR)

        assertEquals(successResponseList[FIRST_RESPONSE].status, charactersLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].status, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].data, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.data)
    }

    @Test
    fun `on getting characters from API returns failure in result, then database return failure too`() {
        val charactersLiveData = charactersViewModel.getCharactersLiveData().testObserver()
        val failureResult = Result.Failure(exception = exception)

        whenever(mockedCharactersService.getCharactersByHouse(GRYFFINDOR)).thenReturn(failureResult)
        whenever(mockedDatabase.getCharactersByHouseName(GRYFFINDOR)).thenReturn(failureResult)

        runBlocking {
            charactersViewModel.fetchCharacters(GRYFFINDOR).join()
        }

        verify(mockedCharactersService).getCharactersByHouse(GRYFFINDOR)
        verify(mockedDatabase).getCharactersByHouseName(GRYFFINDOR)

        assertEquals(failureResponseList[FIRST_RESPONSE].status, charactersLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(failureResponseList[SECOND_RESPONSE].status, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(failureResponseList[SECOND_RESPONSE].error, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.error)
    }
}
