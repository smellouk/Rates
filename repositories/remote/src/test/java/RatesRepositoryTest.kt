import com.google.gson.GsonBuilder
import io.mellouk.repositories.remote.dto.RateList
import io.mellouk.repositories.remote.dto.RateResponse
import io.mellouk.repositories.remote.dto.RatesAdapter
import io.mellouk.repositories.remote.network.repositories.RatesRepository
import io.mellouk.repositories.remote.network.services.RatesService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class RatesRepositoryTest {
    @RelaxedMockK
    lateinit var ratesService: RatesService

    private lateinit var ratesRepository: RatesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        ratesRepository = RatesRepository(ratesService)
    }

    @Test
    fun getRates_ShouldReturnValidListOfRates() {
        every {
            ratesService.getRates(givenCode)
        } returns Single.just(givenResponse)

        ratesRepository.getRates(givenCode)
            .test()
            .apply {
                assertComplete()
                assertNoErrors()
                assertValueCount(1)
                assertValue { response ->
                    response.baseCurrency == givenCode &&
                            response == givenResponse
                }
            }
    }
}

//region given
private const val givenCode = "EUR"
private val gson = GsonBuilder()
    .registerTypeAdapter(
        RateList::class.java,
        RatesAdapter()
    )
    .create()
private const val givenJson = """
    {
        "baseCurrency": "EUR",
        "rates": {
            "AUD": 1.587,
            "BGN": 1.975,
            "BRL": 4.238,
            "CAD": 1.508,
            "CHF": 1.139,
            "CNY": 7.76,
            "CZK": 25.984,
            "DKK": 7.49,
            "GBP": 0.884,
            "HKD": 8.973,
            "HRK": 7.51,
            "HUF": 320.714,
            "DDD": 320.714
        }
    }
    """
private val givenResponse = gson.fromJson(givenJson, RateResponse::class.java)
//endregion given