package io.mellouk.ratesscreen.domain.getrates

import io.mellouk.common.domain.NonParamsBaseUseCase
import io.mellouk.common.models.RateUi
import io.mellouk.offline.model.RateEntity
import io.mellouk.offline.sharedprefs.BaseCurrencyRepository
import io.mellouk.ratesscreen.di.RateListScope
import io.mellouk.ratesscreen.domain.RatesDataState
import io.mellouk.ratesscreen.domain.RatesMapper
import io.mellouk.ratesscreen.domain.SuccessfulRatesState
import io.mellouk.repositories.remote.dto.RateResponse
import io.mellouk.repositories.remote.network.repositories.RemoteRatesRepository
import io.reactivex.Single
import javax.inject.Inject

@RateListScope
class GetRatesUseCase @Inject constructor(
    private val remoteRatesRepository: RemoteRatesRepository,
    private val baseCurrencyRepository: BaseCurrencyRepository,
    private val ratesMapper: RatesMapper
) : NonParamsBaseUseCase<RatesDataState> {
    override fun buildObservable(): Single<RatesDataState> =
        baseCurrencyRepository.current().let { entity ->
            remoteRatesRepository.getRates(entity.currency).map { response ->
                SuccessfulRatesState(
                    createBasedCurrencyRates(response, entity)
                )
            }
        }


    private fun createBasedCurrencyRates(
        response: RateResponse,
        entity: RateEntity
    ): List<RateUi> = with(ratesMapper) {
        val baseCurrency = map(entity)
        map(response, entity.value).apply {
            add(0, baseCurrency)
        }
    }
}