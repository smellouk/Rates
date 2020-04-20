package io.mellouk.ratesscreen.domain

import io.mellouk.common.domain.BaseUseCase
import io.mellouk.ratesscreen.di.RateListScope
import io.mellouk.repositories.remote.network.repositories.RemoteRatesRepository
import io.reactivex.Single
import javax.inject.Inject

@RateListScope
class GetRatesUseCase @Inject constructor(
    private val remoteRatesRepository: RemoteRatesRepository,
    private val ratesMapper: RatesMapper
) : BaseUseCase<GetRatesParams, RatesDataState> {
    override fun buildObservable(params: GetRatesParams): Single<RatesDataState> =
        remoteRatesRepository.getRates(params.code).map { response ->
            SuccessfulRatesState(
                ratesMapper.map(response, params.multiplier)
            )
        }
}