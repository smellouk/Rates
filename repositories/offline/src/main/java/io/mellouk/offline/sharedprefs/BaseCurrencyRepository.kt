package io.mellouk.offline.sharedprefs

import android.content.SharedPreferences
import io.mellouk.offline.model.RateEntity
import io.mellouk.offline.utils.persist
import io.mellouk.offline.utils.restoreBaseCurrency

class BaseCurrencyRepository(
    private val sharedPreferences: SharedPreferences
) {
    private var current: RateEntity? = null

    fun save(rateEntity: RateEntity) {
        rateEntity.persist(sharedPreferences)
        current = rateEntity
    }

    fun current(): RateEntity = current ?: restore()

    private fun restore() = sharedPreferences.restoreBaseCurrency()
}