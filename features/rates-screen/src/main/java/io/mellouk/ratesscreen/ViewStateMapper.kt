package io.mellouk.ratesscreen

import io.mellouk.common.exhaustive
import io.mellouk.ratesscreen.ViewState.*
import io.mellouk.ratesscreen.domain.RatesDataState
import io.mellouk.ratesscreen.domain.SuccessfulRatesState
import io.mellouk.ratesscreen.domain.SuccessfulUpdatedRatesState

class ViewStateMapper {
    fun map(dataState: RatesDataState) = when (dataState) {
        is SuccessfulRatesState -> RateListReady(dataState.rateList)
        is SuccessfulUpdatedRatesState -> RateListUpdate(dataState.rateList)
    }.exhaustive

    fun map(throwable: Throwable?) = Error(throwable?.message)
}