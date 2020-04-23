package io.mellouk.main

import androidx.annotation.IdRes
import androidx.annotation.Keep
import io.mellouk.common.base.BaseViewState

@Keep
sealed class ViewState : BaseViewState {
    object Initial : ViewState()
    object RateList : ViewState() {
        @IdRes
        val destination: Int = R.id.rate_list_fragment
    }
}