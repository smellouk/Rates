package io.mellouk.main

import io.mellouk.common.Commandable
import io.mellouk.common.base.BaseViewModel
import io.mellouk.main.di.MainScope
import io.mellouk.main.ViewState.*
import javax.inject.Inject

@MainScope
class MainViewModel @Inject constructor(
    private val viewStateMapper: ViewStateMapper
) : BaseViewModel<io.mellouk.main.ViewState>(), Commandable<Command> {
    override fun getInitialState() = RateList

    override fun onCommand(cmd: Command) {
        liveData.value = viewStateMapper.map(cmd)
    }
}