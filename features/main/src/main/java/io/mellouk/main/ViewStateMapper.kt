package io.mellouk.main

import io.mellouk.common.exhaustive
import io.mellouk.main.Command.OpenRateList
import io.mellouk.main.ViewState.*
import io.mellouk.main.di.MainScope
import javax.inject.Inject

@MainScope
class ViewStateMapper @Inject constructor() {
    fun map(cmd: Command) = when (cmd) {
        OpenRateList -> RateList
    }.exhaustive
}