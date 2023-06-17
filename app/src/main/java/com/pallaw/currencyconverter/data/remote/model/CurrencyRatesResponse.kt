package com.pallaw.currencyconverter.data.remote.model


import com.google.gson.annotations.SerializedName

data class CurrencyRatesResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val currencyRates: Map<String, Double>,
    @SerializedName("timestamp")
    val timestamp: Int
)