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

import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pallaw.currencyconverter.R
import com.pallaw.currencyconverter.databinding.ExchnageRateActivityBinding
import com.pallaw.currencyconverter.util.Const
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val TAG = "CurrencyConverter"
@AndroidEntryPoint
class ExchangeRateActivity : ComponentActivity() {

    private lateinit var binding: ExchnageRateActivityBinding
    private val viewmodel:ExchangeRateViewModel by viewModels()

    private val convertedAmountListAdapter by lazy { ConvertedAmountListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExchnageRateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup ui data and actions
        setupUi()

        //Observe UI state
        observeUiState()

        //call for initial data
        viewmodel.convertRates(
            binding.edtAmount.text.toString(),
            binding.autoCompleteTextView.text.toString()
        )

    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.exchangeRatesUiState.collect { uiState ->
                    when (uiState) {
                        ExchangeRateUiState.Empty -> Toast.makeText(this@ExchangeRateActivity, "Empty", Toast.LENGTH_SHORT).show()
                        is ExchangeRateUiState.Failure -> Toast.makeText(this@ExchangeRateActivity, "Error ${uiState.errorText}", Toast.LENGTH_SHORT).show()
                        ExchangeRateUiState.Loading -> Toast.makeText(this@ExchangeRateActivity, "Loading Data", Toast.LENGTH_SHORT).show()
                        is ExchangeRateUiState.Success -> {
                            Toast.makeText(this@ExchangeRateActivity, "Success: ${uiState.convertedRates}", Toast.LENGTH_SHORT).show()
                            convertedAmountListAdapter.submitList(uiState.convertedRates)
                        }
                    }
                }
            }
        }
    }

    private fun setupUi() {
        //setup currency code dropdown
        val arrayAdapter = ArrayAdapter(this, R.layout.item_drop_down, Const.currencyCodes)
        binding.autoCompleteTextView.apply {
            inputType = InputType.TYPE_NULL
            setAdapter(arrayAdapter)
            setText(adapter.getItem(0).toString(), false)
            setOnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                viewmodel.convertRates(binding.edtAmount.text.toString(), selectedItem)
            }
        }


        //setup converted amounts list
        binding.rvConvertedAmounts.apply {
            setHasFixedSize(true)
            adapter = convertedAmountListAdapter
            layoutManager = LinearLayoutManager(this@ExchangeRateActivity)
        }

        //setup amount edittext action
        binding.edtAmount.doAfterTextChanged {
            viewmodel.convertRates(
                it?.toString() ?: "",
                binding.autoCompleteTextView.text.toString()
            )
        }
    }
}

