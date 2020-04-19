package io.mellouk.plugin

import Config.kotlinKaptPlugin
import Config.kotlinPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class RatesKotlinModulePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(kotlinPlugin)
        target.plugins.apply(kotlinKaptPlugin)
    }
}