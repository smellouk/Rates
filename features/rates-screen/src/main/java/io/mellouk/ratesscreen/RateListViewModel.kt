package io.mellouk.ratesscreen

import io.mellouk.common.Commandable
import io.mellouk.common.base.BaseViewModel
import io.mellouk.common.exhaustive
import io.mellouk.ratesscreen.Command.*
import io.mellouk.ratesscreen.ViewState.*
import io.mellouk.ratesscreen.di.RateListScope
import io.mellouk.ratesscreen.domain.calculaterates.CalculateRatesParams
import io.mellouk.ratesscreen.domain.calculaterates.CalculateRatesUseCase
import io.mellouk.ratesscreen.domain.getrates.GetRatesUseCase
import io.mellouk.ratesscreen.domain.persistbasecurrency.PersistBaseCurrencyParams
import io.mellouk.ratesscreen.domain.persistbasecurrency.PersistBaseCurrencyUseCase
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RateListScope
class RateListViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val persistBaseCurrencyUseCase: PersistBaseCurrencyUseCase,
    private var calculateRatesUseCase: CalculateRatesUseCase,
    private val viewStateMapper: ViewStateMapper
) : BaseViewModel<ViewState>(), Commandable<Command> {
    private var currentIntervalDisposable: Disposable? = null
    private var currentRateCalculatorDisposable: Disposable? = null

    override fun getInitialState(): ViewState = Initial

    override fun onCommand(cmd: Command) {
        liveData.value = commandHandler(cmd)
    }

    private fun buildObservable() =
        Flowable.interval(FREQUENCY, TimeUnit.SECONDS, Schedulers.io())
            .onBackpressureDrop()
            .retry()
            .flatMapSingle {
                getRatesUseCase.buildObservable()
            }.toObservable()

    private fun commandHandler(cmd: Command) = when (cmd) {
        is RestartRatesWatcher -> onStartRatesWatcher(cmd.isConnected)
        is UpdateBaseCurrency -> onUpdateCurrentBaseCurrency(cmd)
        is CalculateNewRates -> onCalculateNewRates(cmd)
        is StopRatesWatcher -> onStopRatesWatcher()
    }.exhaustive

    private fun onStartRatesWatcher(isConnected: Boolean = true) = if (isConnected) {
        currentIntervalDisposable?.dispose()
        currentIntervalDisposable = addObservable(
            source = buildObservable(),
            onNext = { dataState ->
                liveData.value = viewStateMapper.map(dataState)
            },
            onError = { throwable ->
                liveData.value = viewStateMapper.map(throwable)
            }
        )
        Pending
    } else {
        Error("Network is not available!")
    }

    private fun onCalculateNewRates(cmd: CalculateNewRates): Pending {
        currentRateCalculatorDisposable?.dispose()
        currentRateCalculatorDisposable = addObservable(
            source = calculateRatesUseCase.buildObservable(
                CalculateRatesParams(
                    baseRate = cmd.baseRate,
                    rates = cmd.rates
                )
            ),
            onNext = { dataState ->
                liveData.value = viewStateMapper.map(dataState)
            },
            onError = { throwable ->
                liveData.value = viewStateMapper.map(throwable)
            }
        )
        return Pending
    }

    private fun onUpdateCurrentBaseCurrency(cmd: UpdateBaseCurrency) = with(cmd) {
        addObservable(
            source = calculateRatesUseCase.buildObservable(
                CalculateRatesParams(
                    baseRate = cmd.baseRate,
                    rates = cmd.rates
                )
            ),
            onNext = { dataState ->
                addCompletable(
                    persistBaseCurrencyUseCase.buildObservable(
                        PersistBaseCurrencyParams(baseRate.currency, DEFAULT_MULTIPLIER)
                    )
                )
                liveData.value = viewStateMapper.map(dataState)
            },
            onError = { throwable ->
                liveData.value = viewStateMapper.map(throwable)
            }
        )
        Pending
    }

    private fun onStopRatesWatcher(): Pending {
        currentIntervalDisposable?.dispose()
        return Pending
    }
}

private const val FREQUENCY = 1L