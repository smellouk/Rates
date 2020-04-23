package io.mellouk.repositories.remote.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class RatesDeserializer : JsonDeserializer<RateList> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RateList = RateList().apply {
        toJsonObject(json)?.let { jsonObject ->
            jsonObject.keySet().forEach { code ->
                add(RateDto(toCurrency(code), jsonObject[code].asFloat))
            }
        }
    }

    private fun toJsonObject(json: JsonElement?) = json as? JsonObject?

    private fun toCurrency(code: String) = CurrencyDto.values().firstOrNull { currency ->
        currency.code == code
    } ?: CurrencyDto.UNKNOWN
}