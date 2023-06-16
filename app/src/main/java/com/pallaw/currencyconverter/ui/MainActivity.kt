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
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pallaw.currencyconverter.R
import com.pallaw.currencyconverter.databinding.ItemCurrencyDropdownBinding
import com.pallaw.currencyconverter.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mainActivityBinding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)


        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOf("a", "b", "c"))
        mainActivityBinding.autoCompleteTextView.setAdapter(arrayAdapter)

        val convertedAmountsAdapter = ConvertedAmountsAdapter()
        mainActivityBinding.rvConvertedAmounts.apply {
            setHasFixedSize(true)
            adapter = convertedAmountsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        convertedAmountsAdapter.submitList(
            (1..10).toList().map { ConvertedAmount("Currency $it", it.toDouble()) }
        )
    }
}

data class ConvertedAmount(
    val currency: String,
    val value: Double
)

class ConvertedAmountsAdapter :
    ListAdapter<ConvertedAmount, ConvertedAmountViewHolder>(ConvertedAmountDiff) {
    object ConvertedAmountDiff : DiffUtil.ItemCallback<ConvertedAmount>() {
        override fun areItemsTheSame(oldItem: ConvertedAmount, newItem: ConvertedAmount): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ConvertedAmount,
            newItem: ConvertedAmount
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
    fun bind(item: ConvertedAmount) {
        binding.txtCurrency.text = item.currency
        binding.txtValue.text = item.value.toString()
    }

}
