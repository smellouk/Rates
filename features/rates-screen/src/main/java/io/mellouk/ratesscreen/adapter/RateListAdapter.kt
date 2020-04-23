package io.mellouk.ratesscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.mellouk.common.models.RateUi
import io.mellouk.ratesscreen.DEFAULT_MULTIPLIER
import io.mellouk.ratesscreen.R

class RateListAdapter(
    private val onItemClick: (RateUi) -> Unit,
    private val onBaseCurrencyChanged: (RateUi) -> Unit,
    private val onFocusChanged: (Boolean, RateUi) -> Unit,
    private val defaultBaseCurrencyValue: String = DEFAULT_MULTIPLIER
) : ListAdapter<RateUi, RateViewHolder>(RateItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RateViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.rates_list_item, parent, false
        ).apply {
            setOnClickListener {
                val position = tag as Int
                onItemClick(getItem(position))
            }
        }
    )

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.bind(
            this,
            onBaseCurrencyChanged,
            onFocusChanged,
            defaultBaseCurrencyValue
        )
    }
}