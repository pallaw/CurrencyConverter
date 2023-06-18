package com.pallaw.currencyconverter.util

import com.pallaw.currencyconverter.data.local.database.entity.ExchangeRateEntity
import com.pallaw.currencyconverter.data.remote.model.ExchangeRateDto

fun ExchangeRateDto.toExchangeRateEntity(): ExchangeRateEntity {
    return ExchangeRateEntity(
        base = base,
        timestamp = timestamp,
        rates = rates
    )
}