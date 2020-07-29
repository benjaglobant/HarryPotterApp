package com.globant.harrypotterapp.characterdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.domain.database.HarryPotterDataBase
import com.globant.domain.entity.CharacterDetail
import com.globant.domain.service.CharacterDetailService
import com.globant.domain.usecase.GetCharacterDetailUseCase
import com.globant.domain.usecase.implementation.GetCharacterDetailUseCaseImpl
import com.globant.domain.util.Result
import com.globant.harrypotterapp.viewmodel.CharacterDetailData
import com.globant.harrypotterapp.viewmodel.CharacterDetailViewModel
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.LOADING_CHARACTER_DETAILS
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.SUCCESS_CHARACTER_DETAILS
import com.globant.harrypotterapp.viewmodel.CharacterDetailStatus.ERROR_CHARACTER_DETAILS
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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import test.com.globant.harrypotterapp.testObserver

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

        verify(mockedCharacterDetailService).getCharacterDetail(HARRY_POTTER_ID)

        Assert.assertEquals(successResponseList[ZERO].status, charactersLiveData.observedValues[ZERO]?.peekContent()?.status)
        Assert.assertEquals(successResponseList[ONE].status, charactersLiveData.observedValues[ONE]?.peekContent()?.status)
        Assert.assertEquals(successResponseList[ONE].data, charactersLiveData.observedValues[ONE]?.peekContent()?.data)
    }

    @Test
    fun `when fetchCharacterDetail return failure result`() {
        val charactersLiveData = characterDetailViewModel.getCharacterDetailLiveData().testObserver()

        whenever(mockedCharacterDetailService.getCharacterDetail(WRONG_HARRY_POTTER_ID)).thenReturn(Result.Failure(exception))

        runBlocking {
            characterDetailViewModel.fetchCharacterDetail(WRONG_HARRY_POTTER_ID).join()
        }

        verify(mockedCharacterDetailService).getCharacterDetail(WRONG_HARRY_POTTER_ID)

        Assert.assertEquals(failureResponseList[ZERO].status, charactersLiveData.observedValues[ZERO]?.peekContent()?.status)
        Assert.assertEquals(failureResponseList[ONE].status, charactersLiveData.observedValues[ONE]?.peekContent()?.status)
        Assert.assertEquals(failureResponseList[ONE].error, charactersLiveData.observedValues[ONE]?.peekContent()?.error)
    }

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
        private const val HARRY_POTTER_ID = "5a12292a0f5ae10021650d7e"
        private const val WRONG_HARRY_POTTER_ID = "1"
    }
}
