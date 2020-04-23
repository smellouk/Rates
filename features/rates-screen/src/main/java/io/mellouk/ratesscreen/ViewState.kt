package io.mellouk.ratesscreen

import androidx.annotation.Keep
import io.mellouk.common.base.BaseViewState
import io.mellouk.common.models.RateUi

@Keep
sealed class ViewState : BaseViewState {
    object Initial : ViewState()
    class RateListReady(val rateList: List<RateUi>) : ViewState()
    class RateListUpdate(val rateList: List<RateUi>) : ViewState()
    object Pending : ViewState()
    class Error(val message: String?) : ViewState()
}