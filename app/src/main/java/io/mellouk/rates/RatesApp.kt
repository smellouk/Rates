package io.mellouk.rates

import android.app.Application
import io.mellouk.main.di.MainComponent
import io.mellouk.main.di.MainComponentProvider
import io.mellouk.rates.di.AppComponent
import io.mellouk.rates.di.AppModule
import io.mellouk.rates.di.DaggerAppComponent
import io.mellouk.rates.di.DomainModule
import io.mellouk.ratesscreen.di.RateListComponent
import io.mellouk.ratesscreen.di.RateListComponentProvider

class RatesApp : Application(), MainComponentProvider, RateListComponentProvider {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(
                AppModule(
                    this
                )
            )
            .domainModule(DomainModule(BuildConfig.BASE_URL, BuildConfig.DEBUG))
            .build()
    }

    override fun getMainComponent(): MainComponent = appComponent.getMainComponent()

    override fun getRateListComponent(): RateListComponent = appComponent.getRateListComponent()
}