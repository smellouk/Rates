package io.mellouk.ratesscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.mellouk.common.models.RateUi
import io.mellouk.ratesscreen.R
import io.mellouk.ratesscreen.domain.DEFAULT_MULTIPLIER

class RateListAdapter(
    private val onItemClick: (RateUi) -> Unit,
    private val onBaseCurrencyChanged: (String) -> Unit,
    private val defaultBaseCurrencyValue: String = DEFAULT_MULTIPLIER.toString()
) : ListAdapter<RateUi, RateViewHolder>(RateItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RateViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.rates_list_item, parent, false
        ).apply {
            setOnClickListener {
                onItemClick((tag as RateUi))
            }
        }
    )

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        getItem(position).let { item ->
            holder.itemView.tag = item
            holder.bind(
                item,
                onBaseCurrencyChanged,
                defaultBaseCurrencyValue
            )
        }
    }
}