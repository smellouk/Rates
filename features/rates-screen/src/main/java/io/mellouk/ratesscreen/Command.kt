package io.mellouk.ratesscreen

import io.mellouk.common.base.BaseCommand
import io.mellouk.ratesscreen.domain.GetRatesParams


sealed class Command : BaseCommand {
    class GetRates(val params: GetRatesParams) : Command()
    class UpdateBaseCurrencyValue(val value: String) : Command()
    class RestartRates(val isConnected: Boolean) : Command()
}