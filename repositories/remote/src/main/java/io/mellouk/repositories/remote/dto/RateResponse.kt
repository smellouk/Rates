package io.mellouk.repositories.remote.dto

import com.google.gson.annotations.SerializedName

data class RateResponse(
    @SerializedName("baseCurrency")
    val baseCurrency: String? = null,

    @SerializedName("rates")
    val rates: RateList? = null
)

class RateList : ArrayList<Rate>()
