package io.mellouk.ratesscreen

import io.mellouk.common.exhaustive
import io.mellouk.ratesscreen.ViewState.Error
import io.mellouk.ratesscreen.ViewState.RateListReady
import io.mellouk.ratesscreen.domain.RatesDataState
import io.mellouk.ratesscreen.domain.SuccessfulRatesState

class ViewStateMapper {
    fun map(dataState: RatesDataState) = when (dataState) {
        is SuccessfulRatesState -> RateListReady(dataState.rateList)
    }.exhaustive

    fun map(throwable: Throwable?) = Error(throwable?.message)
}