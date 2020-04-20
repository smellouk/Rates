package io.mellouk.rates.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides

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
}