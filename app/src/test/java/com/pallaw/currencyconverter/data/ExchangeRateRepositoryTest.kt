import com.pallaw.currencyconverter.data.ExchangeRateRepository
import com.pallaw.currencyconverter.data.local.database.dao.ExchangeRateDao
import com.pallaw.currencyconverter.data.local.database.entity.ExchangeRateEntity
import com.pallaw.currencyconverter.data.remote.ExchangeRateService
import com.pallaw.currencyconverter.data.remote.model.ExchangeRateDto
import com.pallaw.currencyconverter.util.toExchangeRateEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class ExchangeRateRepositoryTest {

    private lateinit var repository: ExchangeRateRepository

    @Mock
    private lateinit var exchangeRateDao: ExchangeRateDao

    @Mock
    private lateinit var exchangeRateService: ExchangeRateService

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = ExchangeRateRepository(exchangeRateDao, exchangeRateService)
    }

    @Test
    fun `getExchangeRates returns local data when available`() = runTest(testDispatcher) {
        // Arrange
        val localExchangeRateEntity = ExchangeRateEntity("USD", 123456789L, emptyMap())
        `when`(exchangeRateDao.getExchangeRates()).thenReturn(flowOf(listOf(localExchangeRateEntity)))

        // Act
        val result = repository.getExchangeRates().first()

        // Assert
        assertEquals(localExchangeRateEntity, result.data)
    }

    @Test
    fun `getExchangeRates fetches remote data when local data is outdated`() =
        runTest(testDispatcher) {
            // Arrange
            val localExchangeRateEntity = ExchangeRateEntity("USD", 123456789L, emptyMap())
            val remoteExchangeRateDto = ExchangeRateDto("", "", 987654321L, "USD", emptyMap())
            val remoteExchangeRateEntity = remoteExchangeRateDto.toExchangeRateEntity()

            `when`(exchangeRateDao.getExchangeRates()).thenReturn(
                flowOf(
                    listOf(
                        localExchangeRateEntity
                    )
                )
            )
            `when`(exchangeRateService.getExchangeRates()).thenReturn(
                Response.success(
                    remoteExchangeRateDto
                )
            )
            `when`(exchangeRateDao.insertExchangeRates(listOf(remoteExchangeRateEntity))).thenAnswer {}

            // Act
            // dropping first emission because in this case it will be from local DB
            val result = repository.getExchangeRates().drop(1).first()

            // Assert
            assertEquals(remoteExchangeRateEntity, result.data)
        }

    @Test
    fun `getExchangeRates returns error resource on network error`() = runTest(testDispatcher) {
        // Arrange
        val localExchangeRateEntity = ExchangeRateEntity("USD", 123456789L, emptyMap())

        `when`(exchangeRateDao.getExchangeRates()).thenReturn(flowOf(listOf(localExchangeRateEntity)))
        `when`(exchangeRateService.getExchangeRates()).thenReturn(
            Response.error(
                404,
                "".toResponseBody()
            )
        )

        // Act
        // dropping first emission because in this case it will be from local DB
        val result = repository.getExchangeRates().drop(1).first()

        // Assert
        assertEquals("Failed to fetch exchange rates: ", result.message)
    }
}

