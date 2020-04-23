package io.mellouk.offline.di

import dagger.Component
import io.mellouk.offline.OfflineRepositories
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface OfflineComponent {
    fun inject(offlineRepositories: OfflineRepositories)
}