package io.mellouk.main

import io.mellouk.common.base.BaseViewModel
import io.mellouk.main.di.MainScope
import javax.inject.Inject

@MainScope
class MainViewModel @Inject constructor() : BaseViewModel<ViewState>() {
    override fun getInitialState() = ViewState.Initial
}