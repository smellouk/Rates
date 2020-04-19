package io.mellouk.common.domain

import io.reactivex.Observable

interface BaseUseCase<Params : BaseParams, DataState : BaseDataState> {
    fun buildObservable(params: Params): Observable<DataState>
}