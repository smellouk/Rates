package io.mellouk.repositories.remote.di.component

import dagger.Component
import io.mellouk.repositories.remote.RemoteRepositories
import io.mellouk.repositories.remote.di.module.ClientModule
import io.mellouk.repositories.remote.di.module.RepositoryModule
import io.mellouk.repositories.remote.di.module.ServiceModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ClientModule::class, ServiceModule::class, RepositoryModule::class])
interface RemoteRepositoriesComponent {
    fun inject(repositories: RemoteRepositories)
}