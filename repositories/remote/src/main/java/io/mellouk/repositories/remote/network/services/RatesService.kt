package io.mellouk.repositories.remote.network.services

import io.mellouk.repositories.remote.dto.RateResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesService {
    @GET("/api/android/latest")
    fun getRates(
        @Query("base")
        base: String
    ): Single<RateResponse>
}