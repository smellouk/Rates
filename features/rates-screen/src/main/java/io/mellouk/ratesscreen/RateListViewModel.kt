package io.mellouk.ratesscreen

import io.mellouk.common.Commandable
import io.mellouk.common.base.BaseViewModel
import io.mellouk.common.exhaustive
import io.mellouk.ratesscreen.Command.GetRates
import io.mellouk.ratesscreen.Command.UpdateBaseCurrencyValue
import io.mellouk.ratesscreen.ViewState.Initial
import io.mellouk.ratesscreen.ViewState.Pending
import io.mellouk.ratesscreen.di.RateListScope
import io.mellouk.ratesscreen.domain.GetRatesParams
import io.mellouk.ratesscreen.domain.GetRatesUseCase
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RateListScope
class RateListViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val viewStateMapper: ViewStateMapper
) : BaseViewModel<ViewState>(), Commandable<Command> {
    private var currentGetParams: GetRatesParams = GetRatesParams(
        code = INITIAL_CURRENCY
    )

    init {
        addObservable(
            source = buildObservable(),
            onNext = { dataState ->
                liveData.value = viewStateMapper.map(dataState)
            },
            onError = { throwable ->
                liveData.value = viewStateMapper.map(throwable)
            }
        )
    }

    override fun getInitialState(): ViewState = Initial

    override fun onCommand(cmd: Command) {
        liveData.value = commandHandler(cmd)
    }

    private fun buildObservable() =
        Flowable.interval(FREQUENCY, TimeUnit.SECONDS, Schedulers.io())
            .onBackpressureDrop()
            .flatMapSingle {
                getRatesUseCase.buildObservable(currentGetParams)
            }.toObservable()

    private fun commandHandler(cmd: Command) = when (cmd) {
        is GetRates -> setCurrentGetParam(cmd)
        is UpdateBaseCurrencyValue -> updateCurrentGetParam(cmd)
    }.exhaustive

    private fun setCurrentGetParam(cmd: GetRates): Pending {
        currentGetParams = cmd.params
        return Pending
    }

    private fun updateCurrentGetParam(cmd: UpdateBaseCurrencyValue): Pending {
        currentGetParams = currentGetParams.copy(multiplier = cmd.value.toDouble())
        return Pending
    }
}

private const val FREQUENCY = 1L
private const val INITIAL_CURRENCY = "EUR"