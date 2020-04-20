package io.mellouk.repositories.remote

import io.mellouk.repositories.remote.di.component.DaggerRemoteRepositoriesComponent
import io.mellouk.repositories.remote.di.module.ClientModule
import io.mellouk.repositories.remote.network.repositories.RemoteRatesRepository
import okhttp3.Interceptor
import javax.inject.Inject

class RemoteRepositories(
    isDebug: Boolean = true,
    debugInterceptors: List<Interceptor> = emptyList()
) {
    @Inject
    lateinit var remoteRatesRepository: RemoteRatesRepository

    init {
        DaggerRemoteRepositoriesComponent.builder()
            .clientModule(ClientModule(isDebug, debugInterceptors))
            .build()
            .apply {
                inject(this@RemoteRepositories)
            }
    }
}