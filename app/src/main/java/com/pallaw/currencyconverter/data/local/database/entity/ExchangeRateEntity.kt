package com.pallaw.currencyconverter.data.local.database.entity

import androidx.room.Entity

@Entity(tableName = "exchange_rates", primaryKeys = ["base"])
data class ExchangeRateEntity(
    val base: String,
    val timestamp: Long,
    val rates: Map<String, Double>
)