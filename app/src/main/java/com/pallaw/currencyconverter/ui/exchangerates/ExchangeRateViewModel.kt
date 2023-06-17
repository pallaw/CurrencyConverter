/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pallaw.currencyconverter.ui.exchangerates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pallaw.currencyconverter.ui.model.ConvertedAmount
import com.pallaw.currencyconverter.domain.usecase.GetExchangeRatesUseCase
import com.pallaw.currencyconverter.util.Const
import com.pallaw.currencyconverter.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
) : ViewModel() {

    private val _exchangeRatesUiState = MutableStateFlow<ExchangeRateUiState>(ExchangeRateUiState.Empty)
    val exchangeRatesUiState get() = _exchangeRatesUiState

    init {
        convertRates(
            "1",
            "USD"
        )
    }

    fun convertRates(
        amountStr: String,
        fromCurrency: String
    ) {

        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _exchangeRatesUiState.value = ExchangeRateUiState.Failure("Not a valid amount")
            return
        }

        if (!Const.currencyCodes.contains(fromCurrency.uppercase())) {
            _exchangeRatesUiState.value = ExchangeRateUiState.Failure("Not a valid ")
            return
        }

        viewModelScope.launch {
            _exchangeRatesUiState.value = ExchangeRateUiState.Loading
            getExchangeRatesUseCase.getExchangeRates().collect { exchangeRateResource ->
                when(exchangeRateResource) {
                    is Resource.Error -> _exchangeRatesUiState.value = ExchangeRateUiState.Failure(exchangeRateResource.message!!)
                    is Resource.Success -> {
                        val exchangeRateMap = exchangeRateResource.data!!.rates
                        if (exchangeRateMap.isEmpty()) {
                            _exchangeRatesUiState.value = ExchangeRateUiState.Empty
                        } else {
                            val exchangeRateEntryList = exchangeRateMap.toList()

                            val fromRate = exchangeRateMap[fromCurrency] ?: 0.0
                            val convertedAmountList = exchangeRateEntryList.map {
                                val toRate = it.second
                                val convertedAmount = fromAmount!!.times((fromRate / toRate))
                                val formattedAmount = DecimalFormat("#.##").format(convertedAmount).toDouble()

                                ConvertedAmount(it.first, formattedAmount)
                            }
                            _exchangeRatesUiState.value = ExchangeRateUiState.Success(convertedAmountList)
                        }
                    }
                }
            }
        }
    }

}

sealed class ExchangeRateUiState {
    class Success(val convertedRates: List<ConvertedAmount>) : ExchangeRateUiState()
    class Failure(val errorText: String) : ExchangeRateUiState()
    object Loading : ExchangeRateUiState()
    object Empty : ExchangeRateUiState()
}