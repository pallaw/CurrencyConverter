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
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pallaw.currencyconverter.databinding.ItemCurrencyDropdownBinding
import com.pallaw.currencyconverter.databinding.MainActivityBinding
import com.pallaw.currencyconverter.ui.exchangerates.ExchangeRateUiState
import com.pallaw.currencyconverter.ui.exchangerates.ExchangeRateViewModel
import com.pallaw.currencyconverter.util.Const
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val TAG = "CurrencyConverter"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var binding: MainActivityBinding
    private val viewmodel:ExchangeRateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Const.currencyCodes)
        binding.autoCompleteTextView.apply {
            inputType = InputType.TYPE_NULL
            setAdapter(arrayAdapter)
            setText(adapter.getItem(0).toString(), false)
            setOnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                viewmodel.convertRates(binding.edtAmount.text.toString(), selectedItem)
            }
        }


        val convertedAmountsAdapter = ConvertedAmountsAdapter()
        binding.rvConvertedAmounts.apply {
            setHasFixedSize(true)
            adapter = convertedAmountsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.exchangeRatesUiState.collect { uiState ->
                    when (uiState) {
                        ExchangeRateUiState.Empty -> Toast.makeText(this@MainActivity, "Empty", Toast.LENGTH_SHORT).show()
                        is ExchangeRateUiState.Failure -> Toast.makeText(this@MainActivity, "Error ${uiState.errorText}", Toast.LENGTH_SHORT).show()
                        ExchangeRateUiState.Loading -> Toast.makeText(this@MainActivity, "Loading Data", Toast.LENGTH_SHORT).show()
                        is ExchangeRateUiState.Success -> {
                            Toast.makeText(this@MainActivity, "Success: ${uiState.convertedRates}", Toast.LENGTH_SHORT).show()
                            convertedAmountsAdapter.submitList(uiState.convertedRates)
                        }
                    }
                }
            }
        }



        binding.edtAmount.doAfterTextChanged {
            viewmodel.convertRates(
                it?.toString() ?: "",
                binding.autoCompleteTextView.text.toString()
            )
        }

    }
}

data class ExchangedMoney(
    val currency: String,
    val value: Double
)

class ConvertedAmountsAdapter :
    ListAdapter<ExchangedMoney, ConvertedAmountViewHolder>(ConvertedAmountDiff) {
    object ConvertedAmountDiff : DiffUtil.ItemCallback<ExchangedMoney>() {
        override fun areItemsTheSame(oldItem: ExchangedMoney, newItem: ExchangedMoney): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ExchangedMoney,
            newItem: ExchangedMoney
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
    fun bind(item: ExchangedMoney) {
        binding.txtCurrency.text = item.currency
        binding.txtValue.text = item.value.toString()
    }

}
