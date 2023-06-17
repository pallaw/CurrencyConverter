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

package com.pallaw.currencyconverter.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pallaw.currencyconverter.domain.CurrencyRepository
import com.pallaw.currencyconverter.ui.CurrencyAmount
import com.pallaw.currencyconverter.util.DispatcherProvider
import com.pallaw.currencyconverter.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convertRates(
        amountStr: String,
        fromCurrency: String
    ) {

        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null)
            _conversion.value = CurrencyEvent.Failure("Not a valid ")

        viewModelScope.launch(dispatcher.io) {
            _conversion.value = CurrencyEvent.Loading
            when (val latestRatesResponse = currencyRepository.getLatestRates()) {
                is Resource.Error -> _conversion.value =
                    CurrencyEvent.Failure(latestRatesResponse.message!!)

                is Resource.Success -> {
                    val currencyRateMap = latestRatesResponse.data!!.currencyRates
                    val currencyRateList = currencyRateMap.toList()

                    val fromRate = currencyRateMap[fromCurrency] ?: 0.0
                    val currencyAmountList = currencyRateList.map {
                        val toRate = it.second
                        val convertedAmount = fromAmount!!.times((fromRate / toRate))
                        val formattedAmount = DecimalFormat("#.##").format(convertedAmount).toDouble()

                        CurrencyAmount(it.first, formattedAmount)
                    }
                    _conversion.value = CurrencyEvent.Success(currencyAmountList)

                }
            }
        }
    }


//    val uiState: StateFlow<CurrencyUiState> = currencyRepository
//        .currencys.map<List<String>, CurrencyUiState>(::Success)
//        .catch { emit(Error(it)) }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)
//
//    fun addCurrency(name: String) {
//        viewModelScope.launch {
//            currencyRepository.add(name)
//        }
//    }
//
//    fun convertCurrency(originalValue: String) {
//        TODO("Not yet implemented")
//    }





}

sealed class CurrencyEvent {
    class Success(val convertedRates: List<CurrencyAmount>) : CurrencyEvent()
    class Failure(val errorText: String) : CurrencyEvent()
    object Loading : CurrencyEvent()
    object Empty : CurrencyEvent()
}


sealed interface CurrencyUiState {
    object Loading : CurrencyUiState
    data class Error(val throwable: Throwable) : CurrencyUiState
    data class Success(val data: List<String>) : CurrencyUiState
}
