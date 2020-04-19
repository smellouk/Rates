package io.mellouk.repositories.remote.di.module

import dagger.Module
import dagger.Provides
import io.mellouk.repositories.remote.network.services.RatesService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideRatesService(retrofit: Retrofit): RatesService =
        retrofit.create(RatesService::class.java)
}