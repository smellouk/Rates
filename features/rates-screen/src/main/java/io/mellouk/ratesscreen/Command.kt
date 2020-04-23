package io.mellouk.ratesscreen

import io.mellouk.common.base.BaseCommand
import io.mellouk.common.models.RateUi


sealed class Command : BaseCommand {
    class UpdateBaseCurrency(val baseRate: RateUi, val rates: List<RateUi>) : Command()
    class RestartRatesWatcher(val isConnected: Boolean) : Command()
    class CalculateNewRates(val baseRate: RateUi, val rates: List<RateUi>) : Command()
    object StopRatesWatcher : Command()
}