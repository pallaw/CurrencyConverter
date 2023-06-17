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

package com.pallaw.currencyconverter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pallaw.currencyconverter.databinding.ItemCurrencyDropdownBinding
import com.pallaw.currencyconverter.databinding.MainActivityBinding
import com.pallaw.currencyconverter.ui.currency.CurrencyEvent
import com.pallaw.currencyconverter.ui.currency.CurrencyViewModel
import com.pallaw.currencyconverter.util.Const
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val TAG = "CurrencyConverter"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mainActivityBinding: MainActivityBinding
    private val viewmodel:CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Const.currencyCodes)
        mainActivityBinding.autoCompleteTextView.setAdapter(arrayAdapter)

        val convertedAmountsAdapter = ConvertedAmountsAdapter()
        mainActivityBinding.rvConvertedAmounts.apply {
            setHasFixedSize(true)
            adapter = convertedAmountsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.conversion.collect { uiState ->
                    when (uiState) {
                        CurrencyEvent.Empty -> Log.d(TAG, "empty")
                        is CurrencyEvent.Failure -> Log.d(TAG, "failure: ${uiState.errorText}")
                        CurrencyEvent.Loading -> Log.d(TAG, "loading")
                        is CurrencyEvent.Success -> {
                            Log.d(TAG, "Success: ${uiState.convertedRates}")

                            convertedAmountsAdapter.submitList(uiState.convertedRates)

                        }
                    }
                }
            }
        }

        viewmodel.convertRates(
            "1",
            "USD"
        )


    }
}

data class CurrencyAmount(
    val currency: String,
    val value: Double
)

class ConvertedAmountsAdapter :
    ListAdapter<CurrencyAmount, ConvertedAmountViewHolder>(ConvertedAmountDiff) {
    object ConvertedAmountDiff : DiffUtil.ItemCallback<CurrencyAmount>() {
        override fun areItemsTheSame(oldItem: CurrencyAmount, newItem: CurrencyAmount): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CurrencyAmount,
            newItem: CurrencyAmount
        ): Boolean {
            return oldItem.currency == newItem.currency
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertedAmountViewHolder {
        val binding = ItemCurrencyDropdownBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ConvertedAmountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConvertedAmountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ConvertedAmountViewHolder(private val binding: ItemCurrencyDropdownBinding) :
    ViewHolder(binding.root) {
    fun bind(item: CurrencyAmount) {
        binding.txtCurrency.text = item.currency
        binding.txtValue.text = item.value.toString()
    }

}
