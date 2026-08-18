package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    val results: List<GeoLocationResult>? = null
)

@JsonClass(generateAdapter = true)
data class GeoLocationResult(
    val id: Long? = null,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val elevation: Double? = null,
    @Json(name = "feature_code") val featureCode: String? = null,
    @Json(name = "country_code") val countryCode: String? = null,
    val country: String? = null,
    val admin1: String? = null,
    val admin2: String? = null,
    val timezone: String? = null,
    val population: Long? = null
)
