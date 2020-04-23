package io.mellouk.offline

import android.content.SharedPreferences
import io.mellouk.offline.model.RateEntity
import io.mellouk.offline.sharedprefs.BaseCurrencyRepository
import io.mellouk.offline.utils.CURRENCY_KEY
import io.mellouk.offline.utils.CURRENCY_VALUE
import io.mellouk.offline.utils.DEFAULT_CURRENCY
import io.mellouk.offline.utils.DEFAULT_FLOAT
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BaseCurrencyRepositoryTest {

    @RelaxedMockK
    lateinit var sharedPreferences: SharedPreferences

    @RelaxedMockK
    lateinit var editor: SharedPreferences.Editor

    @InjectMockKs
    lateinit var baseCurrencyRepository: BaseCurrencyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun current_ShouldReturnDefaultRateEntityWhenNoDataIsSaved() {
        every {
            sharedPreferences.getString(CURRENCY_KEY, any())
        } returns DEFAULT_CURRENCY

        every {
            sharedPreferences.getFloat(CURRENCY_VALUE, any())
        } returns DEFAULT_FLOAT

        baseCurrencyRepository.current().apply {
            verify(exactly = 1) {
                sharedPreferences.getString(CURRENCY_KEY, DEFAULT_CURRENCY)
                sharedPreferences.getFloat(CURRENCY_VALUE, DEFAULT_FLOAT)
            }

            assertEquals(currency, DEFAULT_CURRENCY)
            assertEquals(value, DEFAULT_FLOAT)
        }
    }

    @Test
    fun save_ShouldPersistSavedCurrency() {
        every {
            sharedPreferences.edit()
        } returns editor

        every {
            editor.putString(CURRENCY_KEY, givenCurrency)
        } returns editor

        every {
            editor.putFloat(CURRENCY_VALUE, givenValue)
        } returns editor

        baseCurrencyRepository.save(givenRateEntity)

        verify(exactly = 1) {
            editor.putString(CURRENCY_KEY, givenCurrency)
            editor.putFloat(CURRENCY_VALUE, givenValue)
            editor.apply()
        }
    }
}

private const val givenCurrency = "DZD"
private const val givenValue = 10f
private val givenRateEntity = RateEntity(givenCurrency, givenValue)
