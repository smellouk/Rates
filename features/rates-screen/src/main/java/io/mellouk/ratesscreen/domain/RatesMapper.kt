package io.mellouk.ratesscreen.domain

import io.mellouk.common.models.EditableRateUi
import io.mellouk.common.models.NonEditableRateUi
import io.mellouk.common.models.RateUi
import io.mellouk.repositories.remote.dto.RateDto
import io.mellouk.repositories.remote.dto.RateResponse
import java.text.DecimalFormat
import java.util.*

class RatesMapper {
    private val formatter = DecimalFormat(RATE_FORMAT)

    fun map(response: RateResponse, multiplier: Double): List<RateUi> {
        val rates = response.rates?.map { dto ->
            mapToNonEditableRate(dto = dto, multiplier = multiplier)
        }?.toMutableList() ?: mutableListOf()

        rates.add(
            0,
            createEditableRate(
                response.baseCurrency ?: "",
                multiplier
            )
        )
        return rates
    }

    private fun createEditableRate(baseCurrency: String, value: Double): RateUi {
        val name = Currency.getInstance(baseCurrency).displayName.capitalize()
        return EditableRateUi(
            currency = baseCurrency,
            name = name,
            value = value.toFormattedRate()
        )
    }

    private fun mapToNonEditableRate(dto: RateDto, multiplier: Double): RateUi = with(dto) {
        val name = Currency.getInstance(currency.code).displayName.capitalize()
        NonEditableRateUi(
            currency = currency.code,
            name = name,
            value = (value * multiplier).toFormattedRate()
        )
    }

    private fun Double.toFormattedRate() = formatter.format(this)
}

private const val RATE_FORMAT = "##.##"