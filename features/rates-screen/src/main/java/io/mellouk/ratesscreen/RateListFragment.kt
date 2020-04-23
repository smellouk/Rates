package io.mellouk.ratesscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.mellouk.common.base.BaseFragment
import io.mellouk.common.exhaustive
import io.mellouk.common.hide
import io.mellouk.common.models.RateUi
import io.mellouk.common.show
import io.mellouk.common.utils.ConnectivityListener
import io.mellouk.common.utils.NetworkWatcher
import io.mellouk.ratesscreen.Command.*
import io.mellouk.ratesscreen.ViewState.*
import io.mellouk.ratesscreen.adapter.RateListAdapter
import io.mellouk.ratesscreen.di.RateListComponentProvider
import io.mellouk.ratesscreen.di.RateListScope
import io.mellouk.ratesscreen.domain.GetRatesParams
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_rate_list.*
import javax.inject.Inject

@RateListScope
class RateListFragment : BaseFragment<RateListComponentProvider, ViewState, RateListViewModel>(
    R.layout.fragment_rate_list
) {
    @Inject
    lateinit var networkWatcher: NetworkWatcher

    private val onItemClick: (RateUi) -> Unit = { rateUi ->
        requestLatestRates(rateUi.currency)
    }

    private val onBaseCurrencyChanged: (String) -> Unit = { value ->
        viewModel.onCommand(UpdateBaseCurrencyValue(value))
    }

    private val ratesAdapter = RateListAdapter(
        onItemClick,
        onBaseCurrencyChanged
    )

    private val connectivityListener = object : ConnectivityListener {
        override fun onNetworkChanged(isConnected: Boolean) {
            activity?.runOnUiThread {
                viewModel.onCommand(RestartRates(isConnected))
            }
        }
    }

    override fun getViewModelClass() = RateListViewModel::class.java

    override fun inject() {
        componentProvider.getRateListComponent().inject(this)
    }

    override fun onStart() {
        super.onStart()
        networkWatcher.registerListener(connectivityListener)
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

    override fun onDestroy() {
        networkWatcher.unregisterListener(connectivityListener)
        super.onDestroy()
    }

    private fun requestLatestRates(code: String) {
        tvError.hide()
        ratesView.show()
        viewModel.onCommand(
            GetRates(
                GetRatesParams(code = code)
            )
        )
    }

    private fun renderErrorView(state: Error) {
        tvError.text = state.message
        tvError.show()
        ratesView.hide()
        progress.hide()
    }

    private fun renderLoading() {
        progress.show()
    }

    private fun renderRateList(state: RateListReady) {
        progress.hide()
        tvError.hide()
        ratesView.show()
        ratesAdapter.submitList(state.rateList)
    }
}