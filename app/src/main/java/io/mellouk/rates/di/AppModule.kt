package io.mellouk.rates.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import io.mellouk.common.utils.NetworkWatcher

@Module
class AppModule(
    private val context: Context
) {
    @ApplicationScope
    @Provides
    fun provideContext(): Context = context

    @ApplicationScope
    @Provides
    fun provideChucker(): ChuckerInterceptor = ChuckerInterceptor(context)

    @ApplicationScope
    @Provides
    fun provideNetworkWatcher(context: Context) = NetworkWatcher(context)
}