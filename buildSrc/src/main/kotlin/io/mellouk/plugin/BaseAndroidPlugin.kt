package io.mellouk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import Config.kotlinAndroidExtPlugin
import Config.kotlinAndroidPlugin
import Config.kotlinKaptPlugin

abstract class BaseAndroidPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(kotlinAndroidPlugin)
        target.plugins.apply(kotlinAndroidExtPlugin)
        target.plugins.apply(kotlinKaptPlugin)
    }
}