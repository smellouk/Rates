package io.mellouk.repositories.remote.di.module

import dagger.Module
import dagger.Provides
import io.mellouk.repositories.remote.network.repositories.RemoteRatesRepository
import io.mellouk.repositories.remote.network.services.RatesService
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRatesRepository(ratesService: RatesService): RemoteRatesRepository =
        RemoteRatesRepository(ratesService)
}