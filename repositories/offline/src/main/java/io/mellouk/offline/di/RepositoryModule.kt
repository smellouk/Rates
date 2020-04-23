package io.mellouk.offline.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.mellouk.offline.sharedprefs.BaseCurrencyRepository
import javax.inject.Singleton

@Module
class RepositoryModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideBaseCurrencySharedPrefs(): SharedPreferences = context.getSharedPreferences(
        SHARED_PREFS_FILE, Context.MODE_PRIVATE
    )

    @Singleton
    @Provides
    fun provideBaseCurrencyRepository(sharedPreferences: SharedPreferences) =
        BaseCurrencyRepository(sharedPreferences)
}

private const val SHARED_PREFS_FILE = "sharedPrefs"