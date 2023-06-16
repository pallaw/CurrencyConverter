package com.pallaw.currencyconverter.data.remote.model


import com.google.gson.annotations.SerializedName

data class LatestRatesResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Rates,
    @SerializedName("timestamp")
    val timestamp: Int
)