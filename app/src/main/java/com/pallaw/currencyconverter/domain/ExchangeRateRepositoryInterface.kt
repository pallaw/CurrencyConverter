package com.pallaw.currencyconverter.domain

import com.pallaw.currencyconverter.data.local.database.entity.ExchangeRateEntity
import com.pallaw.currencyconverter.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepositoryInterface {
    fun getExchangeRates(): Flow<Resource<ExchangeRateEntity>>
}