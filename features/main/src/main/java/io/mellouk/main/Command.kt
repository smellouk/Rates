package io.mellouk.main

import io.mellouk.common.base.BaseCommand

sealed class Command : BaseCommand {
    object OpenRateList: Command()
}