package com.pallaw.currencyconverter.domain.usecase

import com.pallaw.currencyconverter.data.local.database.entity.ExchangeRateEntity
import com.pallaw.currencyconverter.domain.ExchangeRateRepositoryInterface
import com.pallaw.currencyconverter.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetExchangeRatesUseCaseTest {

    private lateinit var useCase: GetExchangeRatesUseCase

    @Mock
    private lateinit var exchangeRateRepository: ExchangeRateRepositoryInterface

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCase = GetExchangeRatesUseCase(exchangeRateRepository)
    }

    @Test
    fun `getExchangeRates returns data from repository`() = runTest(testDispatcher) {
        // Arrange
        val mockData = ExchangeRateEntity("USD", 123456789L, emptyMap())
        val mockResource = Resource.Success(mockData)
        `when`(exchangeRateRepository.getExchangeRates()).thenReturn(flow { emit(mockResource) })

        // Act
        val result = useCase.getExchangeRates()

        // Assert
        result.collect { resource ->
            assertEquals(mockResource, resource)
        }
    }
}
