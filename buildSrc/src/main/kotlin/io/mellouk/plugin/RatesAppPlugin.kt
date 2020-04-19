package io.mellouk.plugin

import Config.applicationPlugin
import org.gradle.api.Project

class RatesAppPlugin: BaseAndroidPlugin() {
    override fun apply(target: Project) {
        target.plugins.apply(applicationPlugin)
        super.apply(target)
    }
}