package io.mellouk.common.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface BaseUseCase<Params : BaseParams, DataState : BaseDataState> {
    fun buildObservable(params: Params): Observable<DataState>
}

interface BaseSingleUseCase<Params : BaseParams, DataState : BaseDataState> {
    fun buildObservable(params: Params): Single<DataState>
}

interface NonParamsBaseUseCase<DataState : BaseDataState> {
    fun buildObservable(): Single<DataState>
}

interface CompletableBaseUseCase<Params : BaseParams> {
    fun buildObservable(params: Params): Completable
}