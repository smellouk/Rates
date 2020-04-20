package io.mellouk.ratesscreen.domain

import io.mellouk.common.domain.BaseDataState
import io.mellouk.common.models.RateUi

sealed class RatesDataState : BaseDataState

class SuccessfulRatesState(val rateList: List<RateUi>) : RatesDataState()