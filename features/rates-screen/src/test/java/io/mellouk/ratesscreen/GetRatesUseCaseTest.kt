package io.mellouk.ratesscreen

import io.mellouk.ratesscreen.domain.GetRatesParams
import io.mellouk.ratesscreen.domain.GetRatesUseCase
import io.mellouk.ratesscreen.domain.RatesMapper
import io.mellouk.ratesscreen.domain.SuccessfulRatesState
import io.mellouk.repositories.remote.dto.CurrencyDto
import io.mellouk.repositories.remote.dto.RateDto
import io.mellouk.repositories.remote.dto.RateList
import io.mellouk.repositories.remote.dto.RateResponse
import io.mellouk.repositories.remote.network.repositories.RemoteRatesRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetRatesUseCaseTest {
    @RelaxedMockK
    lateinit var remoteRatesRepository: RemoteRatesRepository

    private lateinit var getRatesUseCase: GetRatesUseCase
    private val ratesMapper = RatesMapper()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getRatesUseCase = GetRatesUseCase(remoteRatesRepository, ratesMapper)
    }

    @Test
    fun buildObservable_ShouldEmitSuccessfulDataWithValidRateList() {
        every {
            remoteRatesRepository.getRates(givenCode)
        } returns Single.just(givenResponse)

        getRatesUseCase.buildObservable(givenParams).test()
            .apply {
                assertComplete()
                assertNoErrors()
                assertValueCount(1)
                assertValue { ratesDataState ->
                    ratesDataState is SuccessfulRatesState
                            && ratesDataState.rateList.isNotEmpty()
                }
            }
    }
}

private const val givenCode = "EUR"
private val givenParams = GetRatesParams(givenCode)
private val givenResponse = RateResponse(baseCurrency = givenCode, rates = RateList().apply {
    add(RateDto(CurrencyDto.EUR, 1.0))
})