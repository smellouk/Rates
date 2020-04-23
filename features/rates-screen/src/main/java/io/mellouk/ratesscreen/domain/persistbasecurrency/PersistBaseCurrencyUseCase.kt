package io.mellouk.ratesscreen.domain.persistbasecurrency

import io.mellouk.common.domain.CompletableBaseUseCase
import io.mellouk.offline.sharedprefs.BaseCurrencyRepository
import io.mellouk.ratesscreen.di.RateListScope
import io.mellouk.ratesscreen.domain.RatesMapper
import io.reactivex.Completable
import javax.inject.Inject

@RateListScope
class PersistBaseCurrencyUseCase @Inject constructor(
    private val baseCurrencyRepository: BaseCurrencyRepository,
    private val ratesMapper: RatesMapper
) : CompletableBaseUseCase<PersistBaseCurrencyParams> {
    override fun buildObservable(params: PersistBaseCurrencyParams): Completable =
        Completable.fromRunnable {
            val entity = ratesMapper.map(params.currency, params.value)
            baseCurrencyRepository.save(entity)
        }
}