package io.mellouk.repositories.remote.network.repositories

import io.mellouk.repositories.remote.network.services.RatesService

class RatesRepository(private val ratesService: RatesService) {
    fun getRates(base: String) = ratesService.getRates(base)
}