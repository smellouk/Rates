package io.mellouk.ratesscreen.domain

import io.mellouk.common.models.RateUi
import io.mellouk.common.toSafeFloat
import io.mellouk.offline.model.RateEntity
import io.mellouk.repositories.remote.dto.RateDto
import io.mellouk.repositories.remote.dto.RateResponse
import java.text.DecimalFormat
import java.util.*

class RatesMapper {
    private val formatter = DecimalFormat(RATE_FORMAT)

    fun map(response: RateResponse, multiplier: Float): MutableList<RateUi> =
        response.rates?.map { dto ->
            map(dto = dto, multiplier = multiplier)
        }?.toMutableList() ?: mutableListOf()

    fun map(dto: RateDto, multiplier: Float = 1F): RateUi = with(dto) {
        RateUi(
            currency = currency.code,
            name = currency.code.toCurrencyName(),
            value = (value * multiplier).toFormattedRate()
        )
    }

    fun map(entity: RateEntity): RateUi = with(entity) {
        RateUi(
            currency = currency,
            name = currency.toCurrencyName(),
            value = value.toFormattedRate()
        )
    }

    fun map(currency: String, value: String): RateEntity = RateEntity(
        currency = currency,
        value = value.toSafeFloat()
    )

    private fun Float.toFormattedRate() = formatter.format(this)

    private fun String.toCurrencyName() = Currency.getInstance(this).displayName.capitalize()
}

private const val RATE_FORMAT = "##.##"