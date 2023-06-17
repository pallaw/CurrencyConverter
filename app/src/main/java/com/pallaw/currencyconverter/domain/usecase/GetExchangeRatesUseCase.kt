package com.pallaw.currencyconverter.domain.usecase

import com.pallaw.currencyconverter.data.local.database.entity.ExchangeRateEntity
import com.pallaw.currencyconverter.domain.ExchangeRateRepositoryInterface
import com.pallaw.currencyconverter.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepositoryInterface
) {
    fun getExchangeRates(): Flow<Resource<ExchangeRateEntity>> {
        return exchangeRateRepository.getExchangeRates()
    }
}