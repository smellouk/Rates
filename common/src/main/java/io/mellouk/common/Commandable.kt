package io.mellouk.common

import io.mellouk.common.base.BaseCommand

interface Commandable<Cmd : BaseCommand> {
    fun onCommand(cmd: Cmd)
}