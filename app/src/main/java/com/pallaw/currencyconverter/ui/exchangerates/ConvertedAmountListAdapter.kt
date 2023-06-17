package com.pallaw.currencyconverter.ui.exchangerates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pallaw.currencyconverter.databinding.ItemConvertedAmountBinding
import com.pallaw.currencyconverter.ui.model.ConvertedAmount

class ConvertedAmountListAdapter :
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
        val binding =
            ItemConvertedAmountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConvertedAmountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConvertedAmountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ConvertedAmountViewHolder(private val binding: ItemConvertedAmountBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ConvertedAmount) {
        binding.txtCurrency.text = item.currency
        binding.txtValue.text = item.value.toString()
    }

}