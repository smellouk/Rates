package io.mellouk.ratesscreen.adapter

import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import io.mellouk.common.models.EditableRateUi
import io.mellouk.common.models.RateUi
import io.mellouk.ratesscreen.R
import kotlinx.android.synthetic.main.rates_list_item.view.*

class RateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(
        item: RateUi,
        onBaseCurrencyChanged: (String) -> Unit,
        defaultValue: String
    ) {
        with(itemView) {
            imFlag.setImageResource(resolveImageCurrencyCode(item.currency, context))
            tvCode.text = item.currency
            tvName.text = item.name
            if (item is EditableRateUi) {
                etRate.isEnabled = true
                etRate.doAfterTextChanged { editable ->
                    if (etRate.hasFocus()) {
                        editable?.toString()?.let { value ->
                            if (value.isBlank()) {
                                etRate.setText(defaultValue)
                            } else {
                                item.value = value
                                onBaseCurrencyChanged(value)
                            }
                        }
                    }
                }.apply {
                    etRate.tag = this
                }
            } else {
                etRate.isEnabled = false
                etRate.removeTextWatcher()
            }
            etRate.setText(item.value)
        }
    }

    @DrawableRes
    private fun resolveImageCurrencyCode(code: String, context: Context): Int {
        val imageId = context.resources.getIdentifier(
            "ic_${code.toLowerCase()}",
            "drawable",
            context.packageName
        )

        return if (imageId == 0) R.drawable.ic_error else imageId
    }

    private fun EditText.removeTextWatcher() {
        this.tag?.let { tag ->
            if (tag is TextWatcher) {
                this.removeTextChangedListener(tag)
            }
        }
    }
}