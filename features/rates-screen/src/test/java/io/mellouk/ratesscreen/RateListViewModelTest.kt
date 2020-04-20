package io.mellouk.ratesscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mellouk.ratesscreen.Command.GetRates
import io.mellouk.ratesscreen.ViewState.Initial
import io.mellouk.ratesscreen.ViewState.RateListReady
import io.mellouk.ratesscreen.domain.GetRatesParams
import io.mellouk.ratesscreen.domain.GetRatesUseCase
import io.mellouk.ratesscreen.domain.SuccessfulRatesState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class RateListViewModelTest {
    private val testScheduler = TestScheduler()

    @get:Rule
    val testSchedulerRule = RxImmediateSchedulerRule(testScheduler)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getRatesUseCase: GetRatesUseCase

    @RelaxedMockK
    lateinit var viewStateMapper: ViewStateMapper

    private lateinit var viewModel: RateListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = RateListViewModel(getRatesUseCase, viewStateMapper)
    }

    @Test
    fun initialState_ShouldBeInitial() {
        assertEquals(viewModel.getInitialState(), Initial)
    }

    @Test
    fun onCommandGetRates_ShouldGetLatestRates() {
        every {
            getRatesUseCase.buildObservable(givenGetParams)
        } returns Single.just(givenSuccessfulRatesState)

        every {
            viewStateMapper.map(givenSuccessfulRatesState)
        } returns givenReadyListViewState

        viewModel.onCommand(givenGetCommand)
        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        verify {
            getRatesUseCase.buildObservable(givenGetParams)
            viewStateMapper.map(givenSuccessfulRatesState)
        }
        assertEquals(viewModel.liveData.value, givenReadyListViewState)
    }
}

private const val givenCode = "EUR"
private val givenGetParams = GetRatesParams(code = givenCode)
private val givenGetCommand = GetRates(givenGetParams)
private val givenSuccessfulRatesState = SuccessfulRatesState(listOf())
private val givenReadyListViewState = RateListReady(givenSuccessfulRatesState.rateList)