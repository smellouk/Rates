package io.mellouk.ratesscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mellouk.ratesscreen.ViewState.Initial
import io.mellouk.ratesscreen.ViewState.RateListReady
import io.mellouk.ratesscreen.domain.SuccessfulRatesState
import io.mellouk.ratesscreen.domain.calculaterates.CalculateRatesUseCase
import io.mellouk.ratesscreen.domain.getrates.GetRatesUseCase
import io.mellouk.ratesscreen.domain.persistbasecurrency.PersistBaseCurrencyUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
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
    lateinit var persistBaseCurrencyUseCase: PersistBaseCurrencyUseCase

    @RelaxedMockK
    lateinit var calculateRatesUseCase: CalculateRatesUseCase

    @RelaxedMockK
    lateinit var viewStateMapper: ViewStateMapper

    @InjectMockKs
    lateinit var viewModel: RateListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun initialState_ShouldBeInitial() {
        assertEquals(viewModel.getInitialState(), Initial)
    }

    @Test
    fun onCommandGetRates_ShouldGetLatestRates() {
        every {
            getRatesUseCase.buildObservable()
        } returns Single.just(givenSuccessfulRatesState)

        every {
            viewStateMapper.map(givenSuccessfulRatesState)
        } returns givenReadyListViewState

        viewModel.onCommand(givenGetCommand)
        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        verify {
            getRatesUseCase.buildObservable()
            viewStateMapper.map(givenSuccessfulRatesState)
        }
        assertEquals(viewModel.liveData.value, givenReadyListViewState)
    }
}

private val givenGetCommand = Command.RestartRatesWatcher(true)
private val givenSuccessfulRatesState = SuccessfulRatesState(listOf())
private val givenReadyListViewState = RateListReady(givenSuccessfulRatesState.rateList)