package io.mellouk.rates

import android.app.Application
import io.mellouk.main.di.MainComponent
import io.mellouk.main.di.MainComponentProvider
import io.mellouk.rates.di.AppComponent
import io.mellouk.rates.di.AppModule
import io.mellouk.rates.di.DaggerAppComponent

class RatesApp : Application(), MainComponentProvider {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(
                AppModule(
                    this,
                    BuildConfig.DEBUG
                )
            )
            .build()
    }

    override fun getMainComponent(): MainComponent = appComponent.getMainComponent()
}