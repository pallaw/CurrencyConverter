package com.pallaw.currencyconverter.domain

import com.pallaw.currencyconverter.data.remote.model.LatestRatesResponse
import com.pallaw.currencyconverter.util.Resource

interface CurrencyRepository {
    suspend fun getLatestRates(): Resource<LatestRatesResponse>
}