package io.mellouk.repositories.remote.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class RatesAdapter : JsonDeserializer<RateList> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RateList = RateList().apply {
        toJsonObject(json)?.let { jsonObject ->
            jsonObject.keySet().forEach { code ->
                add(Rate(toCurrency(code), jsonObject[code].asDouble))
            }
        }
    }

    private fun toJsonObject(json: JsonElement?) = json as? JsonObject?

    private fun toCurrency(code: String) = Currency.values().firstOrNull { currency ->
        currency.code == code
    } ?: Currency.UNKNOWN
}