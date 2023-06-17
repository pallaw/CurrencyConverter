package com.pallaw.currencyconverter.data.remote

import com.pallaw.currencyconverter.data.remote.model.ExchangeRateDto
import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRateService {

    @GET("/latest.json?app_id=c8df302360cb4b7b9612c0868908bc19")
    suspend fun getExchangeRates(): Response<ExchangeRateDto>
}