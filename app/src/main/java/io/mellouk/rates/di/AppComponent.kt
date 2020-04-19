package io.mellouk.rates.di

import dagger.Component
import io.mellouk.main.di.MainComponent

@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getMainComponent(): MainComponent
}