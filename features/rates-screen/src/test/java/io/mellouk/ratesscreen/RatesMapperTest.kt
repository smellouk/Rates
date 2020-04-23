package io.mellouk.ratesscreen

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
            assertTrue(rateList.last().value.toFloat() / givenValue == giveMultiplier)
            assertTrue(rateList.last().name == expectedName)
        }
    }
}

private const val givenCode = "AUD"
private const val givenValue = 3f
private const val giveMultiplier = 100f
private const val expectedName = "Australian Dollar"
private val givenResponse = RateResponse(baseCurrency = givenCode, rates = RateList().apply {
    add(RateDto(CurrencyDto.AUD, givenValue))
})