import com.google.gson.JsonParser
import io.mellouk.repositories.remote.dto.Currency
import io.mellouk.repositories.remote.dto.RateList
import io.mellouk.repositories.remote.dto.RatesAdapter
import org.junit.Assert.assertTrue
import org.junit.Test

class RatesAdapterTest {
    private val ratesAdapter = RatesAdapter()

    @Test
    fun deserialize_ShouldDeserializeRatesCorrectly() {
        ratesAdapter.deserialize(givenJsonObject, null, null).apply {
            assertTrue(isNotEmpty())
            assertTrue(hasUnknownCurrency(this))
        }
    }

    private fun hasUnknownCurrency(rateList: RateList): Boolean {
        val rate = rateList.firstOrNull { rate ->
            rate.currency == Currency.UNKNOWN
        }

        return rate != null
    }
}

private const val givenJson = """
        {
            "EUR": 1.587,
            "BGN": 1.975,
            "BRL": 4.238,
            "CAD": 1.508,
            "CHF": 1.139,
            "CNY": 7.76,
            "CZK": 25.984,
            "DDD": 134.99 //Unknown Currency
        }
    """
private val givenJsonObject = JsonParser().parse(givenJson).asJsonObject