package io.mellouk.rates.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import io.mellouk.repositories.remote.RemoteRepositories
import okhttp3.Interceptor

@Module
class AppModule(
    private val context: Context,
    private val isDebug: Boolean
) {
    @ApplicationScope
    @Provides
    fun provideContext(): Context = context

    @ApplicationScope
    @Provides
    fun provideChucker(): ChuckerInterceptor = ChuckerInterceptor(context)

    @ApplicationScope
    @Provides
    fun provideRemoteRepositories(chuckerInterceptor: ChuckerInterceptor): RemoteRepositories =
        RemoteRepositories(
            isDebug = isDebug,
            debugInterceptors = listOf<Interceptor>(chuckerInterceptor)
        )
}