package io.mellouk.ratesscreen.domain

import io.mellouk.common.domain.BaseParams

data class GetRatesParams(
    val code: String,
    val multiplier: Double = DEFAULT_MULTIPLIER
) : BaseParams

const val DEFAULT_MULTIPLIER = 1.0