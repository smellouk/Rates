package io.mellouk.common.domain

import io.reactivex.Single

interface BaseUseCase<Params : BaseParams, DataState : BaseDataState> {
    fun buildObservable(params: Params): Single<DataState>
}