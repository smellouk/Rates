package io.mellouk.ratesscreen.domain.calculaterates

import io.mellouk.common.domain.BaseUseCase
import io.mellouk.common.models.RateUi
import io.mellouk.offline.model.RateEntity
import io.mellouk.offline.sharedprefs.BaseCurrencyRepository
import io.mellouk.ratesscreen.domain.RatesDataState
import io.mellouk.ratesscreen.domain.RatesMapper
import io.mellouk.ratesscreen.domain.SuccessfulUpdatedRatesState
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CalculateRatesUseCase @Inject constructor(
    private val ratesMapper: RatesMapper,
    private val baseCurrencyRepository: BaseCurrencyRepository
) : BaseUseCase<CalculateRatesParams, RatesDataState> {
    override fun buildObservable(params: CalculateRatesParams): Observable<RatesDataState> =
        Single.just(params).toObservable().map {
            with(params) {
                val oldBaseRateEntity = baseCurrencyRepository.current()
                val newBaseEntity = baseRate.toEntity()
                val entities = rates.toEntities()

                val newRates = if (oldBaseRateEntity.isSameCurrency(newBaseEntity)) {
                    multiplyTheCurrentRates(oldBaseRateEntity, newBaseEntity, entities)
                } else {
                    convertToOtherBaseCurrencyRates(oldBaseRateEntity, newBaseEntity, entities)
                }

                createDataState(newRates)
            }
        }

    private fun multiplyTheCurrentRates(
        oldBaseRateEntity: RateEntity,
        newBaseRateEntity: RateEntity,
        entities: MutableList<RateEntity>
    ): List<RateEntity> {
        baseCurrencyRepository.save(newBaseRateEntity)
        return normalizeNewRateList(oldBaseRateEntity, newBaseRateEntity, entities)
    }

    private fun normalizeNewRateList(
        oldBaseRateEntity: RateEntity,
        newBaseRateEntity: RateEntity,
        entities: MutableList<RateEntity>
    ): List<RateEntity> = calculateNewRateList(
        oldBaseRateEntity,
        newBaseRateEntity,
        entities
    ).addBaseCurrency(newBaseRateEntity)


    private fun convertToOtherBaseCurrencyRates(
        oldBaseRateEntity: RateEntity,
        newBaseRateEntity: RateEntity,
        entities: MutableList<RateEntity>
    ): List<RateEntity> {
        val newBasedCurrencyRate = 1f * oldBaseRateEntity.value / newBaseRateEntity.value
        val otherNewBaseCurrencyEntity = newBaseRateEntity.copy(value = newBasedCurrencyRate)
        baseCurrencyRepository.save(newBaseRateEntity)
        return convertToNewBasedRateList(oldBaseRateEntity, otherNewBaseCurrencyEntity, entities)
    }

    private fun convertToNewBasedRateList(
        oldBaseRateEntity: RateEntity,
        newBaseRateEntity: RateEntity,
        entities: MutableList<RateEntity>
    ) = calculateNewRateList(
        oldBaseRateEntity,
        newBaseRateEntity,
        entities
    ).addBaseCurrency(newBaseRateEntity.copy(value = 1f))

    private fun calculateNewRateList(
        oldBaseRateEntity: RateEntity,
        newBaseRateEntity: RateEntity,
        entities: MutableList<RateEntity>
    ): MutableList<RateEntity> {
        entities.removeAll {
            it.currency == newBaseRateEntity.currency
        }

        return entities.map { rateEntity ->
            rateEntity.copy(
                value = calculateNewRateValue(
                    oldBaseRateEntity,
                    newBaseRateEntity,
                    rateEntity
                )
            )
        }.sortedBy {
            it.currency
        }.toMutableList()
    }

    private fun calculateNewRateValue(
        oldBaseRateEntity: RateEntity,
        newBaseRateEntity: RateEntity,
        rateEntity: RateEntity
    ) = rateEntity.value / oldBaseRateEntity.value * newBaseRateEntity.value

    private fun createDataState(entities: List<RateEntity>) =
        SuccessfulUpdatedRatesState(
            entities.map { entity ->
                ratesMapper.map(entity)
            }
        )

    private fun RateUi.toEntity() = ratesMapper.map(currency, value)

    private fun List<RateUi>.toEntities() = map { rateUi ->
        rateUi.toEntity()
    }.toMutableList()

    private fun RateEntity.isSameCurrency(other: RateEntity) = currency == other.currency

    private fun MutableList<RateEntity>.addBaseCurrency(baseCurrency: RateEntity) = this.apply {
        this.add(0, baseCurrency)
    }
}