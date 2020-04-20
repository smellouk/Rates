package io.mellouk.ratesscreen

import io.mellouk.common.models.EditableRateUi
import io.mellouk.ratesscreen.domain.RatesMapper
import io.mellouk.repositories.remote.dto.CurrencyDto
import io.mellouk.repositories.remote.dto.RateDto
import io.mellouk.repositories.remote.dto.RateList
import io.mellouk.repositories.remote.dto.RateResponse
import org.junit.Assert.assertTrue
import org.junit.Test

class RatesMapperTest {
    private val ratesMapper = RatesMapper()

    @Test
    fun map_ShouldConvertDtoToRateUiModel() {
        ratesMapper.map(givenResponse, giveMultiplier).let { rateList ->
            assertTrue(rateList.isNotEmpty())
            assertTrue(rateList.first().currency == givenCode)
            assertTrue(rateList.filterIsInstance<EditableRateUi>().size == 1)
            assertTrue(rateList.last().value.toDouble() / givenValue == giveMultiplier)
            assertTrue(rateList.last().name == expectedName)
        }
    }
}

private const val givenCode = "EUR"
private const val givenValue = 3.0
private const val giveMultiplier = 100.0
private const val expectedName = "Australian Dollar"
private val givenResponse = RateResponse(baseCurrency = givenCode, rates = RateList().apply {
    add(RateDto(CurrencyDto.AUD, givenValue))
})