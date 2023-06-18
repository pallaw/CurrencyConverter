package com.pallaw.currencyconverter.ui.exchangerates

import com.pallaw.currencyconverter.ui.model.ConvertedAmount

sealed class ExchangeRateUiState {
    class Success(val convertedRates: List<ConvertedAmount>) : ExchangeRateUiState()
    class Failure(val errorText: String) : ExchangeRateUiState()
    object Loading : ExchangeRateUiState()
    object Empty : ExchangeRateUiState()
}