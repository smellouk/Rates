package io.mellouk.plugin

import Config.libraryPlugin
import org.gradle.api.Project

class RatesAndroidModulePlugin: BaseAndroidPlugin() {
    override fun apply(target: Project) {
        target.plugins.apply(libraryPlugin)
        super.apply(target)
    }
}