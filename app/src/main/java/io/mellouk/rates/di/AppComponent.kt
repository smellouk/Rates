package io.mellouk.rates.di

import dagger.Component
import io.mellouk.main.di.MainComponent
import io.mellouk.ratesscreen.di.RateListComponent

@ApplicationScope
@Component(modules = [AppModule::class, DomainModule::class])
interface AppComponent {
    fun getMainComponent(): MainComponent
    fun getRateListComponent(): RateListComponent
}