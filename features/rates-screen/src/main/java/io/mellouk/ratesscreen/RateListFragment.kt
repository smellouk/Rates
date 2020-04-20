package io.mellouk.ratesscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.mellouk.common.base.BaseFragment
import io.mellouk.common.exhaustive
import io.mellouk.common.hide
import io.mellouk.common.models.RateUi
import io.mellouk.common.show
import io.mellouk.ratesscreen.Command.GetRates
import io.mellouk.ratesscreen.Command.UpdateBaseCurrencyValue
import io.mellouk.ratesscreen.ViewState.*
import io.mellouk.ratesscreen.adapter.RateListAdapter
import io.mellouk.ratesscreen.di.RateListComponentProvider
import io.mellouk.ratesscreen.domain.GetRatesParams
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_rate_list.*


class RateListFragment : BaseFragment<RateListComponentProvider, ViewState, RateListViewModel>(
    R.layout.fragment_rate_list
) {
    private val onItemClick: (RateUi) -> Unit = { rateUi ->
        requestLatestRatesView(rateUi.currency)
    }

    private val onBaseCurrencyChanged: (String) -> Unit = { value ->
        viewModel.onCommand(UpdateBaseCurrencyValue(value))
    }

    private val ratesAdapter = RateListAdapter(
        onItemClick,
        onBaseCurrencyChanged
    )

    override fun getViewModelClass() = RateListViewModel::class.java

    override fun inject() {
        componentProvider.getRateListComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                rvRates.scrollToPosition(0)
            }
        })
        rvRates.setOnTouchListener { v, _ ->
            v.hideKeyboard()
            false
        }
        rvRates.adapter = ratesAdapter
        rvRates.itemAnimator = null
    }

    override fun renderViewState(state: ViewState) {
        when (state) {
            is Initial -> renderLoading()
            is Error -> renderErrorView(state)
            is Pending -> renderDefaultViewState()
            is RateListReady -> renderRateList(state)
        }.exhaustive
    }

    private fun requestLatestRatesView(code: String) {
        errorView.hide()
        rvRates.show()
        viewModel.onCommand(
            GetRates(
                GetRatesParams(code = code)
            )
        )
    }

    private fun renderErrorView(state: Error) {
        tvError.text = state.message
        rvRates.hide()
        errorView.show()
        progress.hide()
    }

    private fun renderLoading() {
        progress.show()
    }

    private fun renderRateList(state: RateListReady) {
        progress.hide()
        ratesAdapter.submitList(state.rateList)
    }
}