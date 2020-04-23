package io.mellouk.ratesscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
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
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_rate_list.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import javax.inject.Inject


@RateListScope
class RateListFragment : BaseFragment<RateListComponentProvider, ViewState, RateListViewModel>(
    R.layout.fragment_rate_list
) {
    @Inject
    lateinit var networkWatcher: NetworkWatcher

    private lateinit var keyboardVisibilityEvent: Unregistrar

    private var itemAnimator: RecyclerView.ItemAnimator = DefaultItemAnimator()

    private val onItemClick: (RateUi) -> Unit = { selectedRate ->
        viewModel.onCommand(StopRatesWatcher)
        updateBaseCurrency(selectedRate)
    }

    private val onBaseCurrencyChanged: (RateUi) -> Unit = { rate ->
        viewModel.onCommand(StopRatesWatcher)
        calculateNewRates(rate)
    }

    private val onFocusChanged: (Boolean, RateUi) -> Unit = { hasFocus, rateUi ->
        if (hasFocus) {
            viewModel.onCommand(StopRatesWatcher)
            calculateNewRates(rateUi)
        }
    }

    private val ratesAdapter = RateListAdapter(
        onItemClick,
        onBaseCurrencyChanged,
        onFocusChanged
    )

    private val connectivityListener = object : ConnectivityListener {
        override fun onNetworkChanged(isConnected: Boolean) {
            activity?.runOnUiThread {
                viewModel.onCommand(RestartRatesWatcher(isConnected))
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
        rvRates.apply {
            ratesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    scrollToPosition(0)
                }
            })

            setOnTouchListener { v, _ ->
                clearCurrentFocus()
                v.hideKeyboard()
                false
            }

            adapter = ratesAdapter
            itemAnimator = null
        }

        keyboardVisibilityEvent =
            KeyboardVisibilityEvent.registerEventListener(activity) { isOpen ->
                if (!isOpen) {
                    clearCurrentFocus()
                }
            }
    }

    override fun renderViewState(state: ViewState) {
        when (state) {
            is Initial -> renderLoading()
            is Error -> renderErrorView(state)
            is Pending -> renderDefaultViewState()
            is RateListReady -> renderRateList(state.rateList)
            is RateListUpdate -> renderUpdatedRateList(state)
        }.exhaustive
    }

    override fun onPause() {
        viewModel.onCommand(StopRatesWatcher)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onCommand(RestartRatesWatcher(true))
    }

    override fun onDestroy() {
        keyboardVisibilityEvent.unregister()
        networkWatcher.unregisterListener(connectivityListener)
        super.onDestroy()
    }

    private fun updateBaseCurrency(selectedRate: RateUi) {
        tvError.hide()
        ratesView.show()
        viewModel.onCommand(UpdateBaseCurrency(selectedRate, ratesAdapter.currentList))
    }

    private fun calculateNewRates(rate: RateUi) {
        tvError.hide()
        ratesView.show()
        viewModel.onCommand(CalculateNewRates(rate, ratesAdapter.currentList))
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

    private fun renderRateList(rateList: List<RateUi>) {
        progress.hide()
        tvError.hide()
        ratesView.show()
        rvRates.post {
            ratesAdapter.submitList(rateList)
        }
    }

    private fun renderUpdatedRateList(state: RateListUpdate) {
        rvRates.itemAnimator = itemAnimator
        ratesAdapter.submitList(state.rateList)
        rvRates.post {
            viewModel.onCommand(RestartRatesWatcher(true))
        }
    }

    private fun clearCurrentFocus() {
        activity?.currentFocus?.clearFocus()
    }
}

const val DEFAULT_MULTIPLIER = "1"