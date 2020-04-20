package io.mellouk.ratesscreen.di

import dagger.Module
import dagger.Provides
import io.mellouk.ratesscreen.ViewStateMapper
import io.mellouk.ratesscreen.domain.RatesMapper

@Module
class PresentationModule {
    @Provides
    @RateListScope
    fun provideRatesMapper() = RatesMapper()

    @Provides
    @RateListScope
    fun provideViewStateMapper() = ViewStateMapper()
}