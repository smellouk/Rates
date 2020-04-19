plugins {
    `kotlin-dsl`
}

object PluginVersion {
    const val android = "3.6.1"
    const val kotlin = "1.3.61"
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:${PluginVersion.android}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersion.kotlin}")
}

gradlePlugin {
    plugins {
        register("RatesAppPlugin") {
            id = "rates-app"
            implementationClass = "io.mellouk.plugin.RatesAppPlugin"
        }
        register("RatesAndroidModulePlugin") {
            id = "rates-android-module"
            implementationClass = "io.mellouk.plugin.RatesAndroidModulePlugin"
        }
        register("RatesKotlinModulePlugin") {
            id = "rates-kotlin-module"
            implementationClass = "io.mellouk.plugin.RatesKotlinModulePlugin"
        }
    }
}