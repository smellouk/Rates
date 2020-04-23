package io.mellouk.offline

import android.content.Context
import io.mellouk.offline.di.DaggerOfflineComponent
import io.mellouk.offline.di.RepositoryModule
import io.mellouk.offline.sharedprefs.BaseCurrencyRepository
import javax.inject.Inject

class OfflineRepositories(context: Context) {
    @Inject
    lateinit var baseCurrencyRepository: BaseCurrencyRepository

    init {
        DaggerOfflineComponent.builder()
            .repositoryModule(RepositoryModule(context))
            .build().apply {
                inject(this@OfflineRepositories)
            }
    }
}