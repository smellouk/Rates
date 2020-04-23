package io.mellouk.ratesscreen.domain.calculaterates

import io.mellouk.common.domain.BaseParams
import io.mellouk.common.models.RateUi

class CalculateRatesParams(
    val baseRate: RateUi, val rates: List<RateUi>
) : BaseParams