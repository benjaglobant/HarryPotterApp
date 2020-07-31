package com.globant.harrypotterapp.characterdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.CharacterDetail
import com.globant.domain.service.CharacterDetailService
import com.globant.domain.usecase.GetCharacterDetailUseCase
import com.globant.domain.usecase.implementation.GetCharacterDetailUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.viewmodel.CharacterDetailData
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.ERROR_CHARACTER_DETAILS
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.LOADING_CHARACTER_DETAILS
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.SUCCESS_CHARACTER_DETAILS
import com.globant.harrypotterapp.viewmodel.CharacterDetailViewModel
import com.globant.harrypotterapp.viewmodel.contract.CharacterDetailContract
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
class CharacterDetailViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var characterDetailViewModel: CharacterDetailContract.ViewModel
    private lateinit var getCharacterDetailUseCase: GetCharacterDetailUseCase
    private val mockedCharacterDetailService: CharacterDetailService = mock()
    private val mockedDatabase: HarryPotterDataBase = mock()
    private val characterDetail: CharacterDetail = mock()
    private val exception: Exception = mock()
    private val successResponseList = listOf(
        CharacterDetailData(status = LOADING_CHARACTER_DETAILS),
        CharacterDetailData(status = SUCCESS_CHARACTER_DETAILS, data = characterDetail)
    )
    private val failureResponseList: List<CharacterDetailData<CharacterDetail>> = listOf(
        CharacterDetailData(status = LOADING_CHARACTER_DETAILS),
        CharacterDetailData(status = ERROR_CHARACTER_DETAILS, error = exception)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCharacterDetailUseCase = GetCharacterDetailUseCaseImpl(mockedCharacterDetailService, mockedDatabase)
        characterDetailViewModel = CharacterDetailViewModel(getCharacterDetailUseCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when fetchCharacterDetail return success result`() {
        val charactersLiveData = characterDetailViewModel.getCharacterDetailLiveData().testObserver()

        whenever(mockedCharacterDetailService.getCharacterDetail(HARRY_POTTER_ID)).thenReturn(Result.Success(characterDetail))

        runBlocking {
            characterDetailViewModel.fetchCharacterDetail(HARRY_POTTER_ID).join()
        }

        successResponseList[SECOND_RESPONSE].data?.let { verify(mockedDatabase).updateCharacterDetail(it) }
        verify(mockedCharacterDetailService).getCharacterDetail(HARRY_POTTER_ID)

        assertEquals(successResponseList[FIRST_RESPONSE].status, charactersLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].status, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].data, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.data)
    }

    @Test
    fun `when fetchCharacterDetail return failure result, then gets character detail from database`() {
        val charactersLiveData = characterDetailViewModel.getCharacterDetailLiveData().testObserver()

        whenever(mockedCharacterDetailService.getCharacterDetail(HARRY_POTTER_ID)).thenReturn(Result.Failure(exception))
        whenever(mockedDatabase.getCharacterDetail(HARRY_POTTER_ID)).thenReturn(Result.Success(data = characterDetail))

        runBlocking {
            characterDetailViewModel.fetchCharacterDetail(HARRY_POTTER_ID).join()
        }

        verify(mockedCharacterDetailService).getCharacterDetail(HARRY_POTTER_ID)
        verify(mockedDatabase).getCharacterDetail(HARRY_POTTER_ID)

        assertEquals(successResponseList[FIRST_RESPONSE].status, charactersLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].status, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(successResponseList[SECOND_RESPONSE].data, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.data)
    }

    @Test
    fun `when fetchCharacterDetail return failure result, then database returns failure too`() {
        val charactersLiveData = characterDetailViewModel.getCharacterDetailLiveData().testObserver()
        val failureResult = Result.Failure(exception)
        whenever(mockedCharacterDetailService.getCharacterDetail(WRONG_HARRY_POTTER_ID)).thenReturn(failureResult)
        whenever(mockedDatabase.getCharacterDetail(WRONG_HARRY_POTTER_ID)).thenReturn(failureResult)

        runBlocking {
            characterDetailViewModel.fetchCharacterDetail(WRONG_HARRY_POTTER_ID).join()
        }

        verify(mockedCharacterDetailService).getCharacterDetail(WRONG_HARRY_POTTER_ID)
        verify(mockedDatabase).getCharacterDetail(WRONG_HARRY_POTTER_ID)

        assertEquals(failureResponseList[FIRST_RESPONSE].status, charactersLiveData.observedValues[FIRST_RESPONSE]?.peekContent()?.status)
        assertEquals(failureResponseList[SECOND_RESPONSE].status, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.status)
        assertEquals(failureResponseList[SECOND_RESPONSE].error, charactersLiveData.observedValues[SECOND_RESPONSE]?.peekContent()?.error)
    }

    companion object {
        private const val HARRY_POTTER_ID = "5a12292a0f5ae10021650d7e"
        private const val WRONG_HARRY_POTTER_ID = "1"
    }
}
