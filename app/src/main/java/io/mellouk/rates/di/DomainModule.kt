package io.mellouk.rates.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import io.mellouk.offline.OfflineRepositories
import io.mellouk.repositories.remote.RemoteRepositories
import okhttp3.Interceptor

@Module
class DomainModule(private val hostUrl: String, private val isDebug: Boolean) {
    @ApplicationScope
    @Provides
    fun provideRemoteRepositories(chuckerInterceptor: ChuckerInterceptor): RemoteRepositories =
        RemoteRepositories(
            hostUrl,
            isDebug = isDebug,
            debugInterceptors = listOf<Interceptor>(chuckerInterceptor)
        )


    @ApplicationScope
    @Provides
    fun provideOfflineRepositories(context: Context): OfflineRepositories =
        OfflineRepositories(context)

    @ApplicationScope
    @Provides
    fun provideRatesRepository(repositories: RemoteRepositories) =
        repositories.remoteRatesRepository

    @ApplicationScope
    @Provides
    fun provideBaseCurrencyRepository(repositories: OfflineRepositories) =
        repositories.baseCurrencyRepository
}