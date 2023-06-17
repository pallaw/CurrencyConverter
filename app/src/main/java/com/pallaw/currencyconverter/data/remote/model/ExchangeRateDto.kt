package com.pallaw.currencyconverter.data.remote.model

data class ExchangeRateDto(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)