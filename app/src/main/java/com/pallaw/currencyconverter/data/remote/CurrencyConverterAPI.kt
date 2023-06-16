package com.pallaw.currencyconverter.data.remote

import com.pallaw.currencyconverter.data.remote.model.LatestRatesResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyConverterAPI {

    @GET("/latest.json")
    suspend fun getLatestRates(): Response<LatestRatesResponse>
}