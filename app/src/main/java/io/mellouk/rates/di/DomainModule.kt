package io.mellouk.rates.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import io.mellouk.repositories.remote.RemoteRepositories
import okhttp3.Interceptor

@Module
class DomainModule(private val isDebug: Boolean) {
    @ApplicationScope
    @Provides
    fun provideGitHubRepositories(chuckerInterceptor: ChuckerInterceptor): RemoteRepositories =
        RemoteRepositories(
            isDebug = isDebug,
            debugInterceptors = listOf<Interceptor>(chuckerInterceptor)
        )


    @ApplicationScope
    @Provides
    fun provideRatesRepository(repositories: RemoteRepositories) =
        repositories.remoteRatesRepository
}