package io.mellouk.common.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseFragment<
        ComponentProvider : BaseComponentProvider,
        State : BaseViewState,
        ViewModel : BaseViewModel<State>
        >(layout: Int) :
    Fragment(layout) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var componentProvider: ComponentProvider

    lateinit var viewModel: ViewModel

    override fun onStart() {
        super.onStart()
        componentProvider =
            activity?.application as? ComponentProvider ?: throw IllegalArgumentException(
                "Application did not implement component provider!"
            )
        inject()

        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider[getViewModelClass()]
        startObserving()
        retainInstance = true
    }

    abstract fun getViewModelClass(): Class<out ViewModel>

    override fun onStop() {
        super.onStop()
        viewModel.liveData.removeObservers(this)
    }

    /**
     * Use this method if you have a state without view rendering
     */
    open fun renderDefaultViewState() {
        // NO OP
    }

    abstract fun inject()

    abstract fun renderViewState(state: State)

    fun startObserving() {
        viewModel.liveData.observe(
            this,
            Observer { renderViewState(it) }
        )
    }

    fun stopObserving() {
        viewModel.liveData.removeObservers(this)
    }
}