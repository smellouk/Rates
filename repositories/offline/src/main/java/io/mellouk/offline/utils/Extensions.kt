package io.mellouk.offline.utils

import android.content.SharedPreferences
import io.mellouk.offline.model.RateEntity

fun RateEntity.persist(sharedPreferences: SharedPreferences) {
    sharedPreferences.edit()
        .putString(CURRENCY_KEY, this@persist.currency)
        .putFloat(CURRENCY_VALUE, this@persist.value)
        .apply()
}

fun SharedPreferences.restoreBaseCurrency() = RateEntity(
    currency = getString(CURRENCY_KEY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY,
    value = getFloat(CURRENCY_VALUE, DEFAULT_FLOAT)
)

const val CURRENCY_KEY = "CURRENCY_KEY"
const val CURRENCY_VALUE = "CURRENCY_VALUE"
const val DEFAULT_CURRENCY = "EUR"
const val DEFAULT_FLOAT = 1F