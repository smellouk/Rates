package io.mellouk.ratesscreen.adapter

import androidx.recyclerview.widget.DiffUtil
import io.mellouk.common.models.RateUi

class RateItemDiffCallback : DiffUtil.ItemCallback<RateUi>() {
    override fun areItemsTheSame(oldItem: RateUi, newItem: RateUi) =
        oldItem.currency == newItem.currency

    override fun areContentsTheSame(oldItem: RateUi, newItem: RateUi) =
        oldItem.value == newItem.value
}