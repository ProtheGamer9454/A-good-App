package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val country: String,
    val admin1: String? = null,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val isCurrentLocation: Boolean = false,
    val isFavorite: Boolean = true,
    val orderIndex: Int = 0,
    val lastUpdated: Long = 0L,
    val cachedTemp: Double? = null,
    val cachedWeatherCode: Int? = null,
    val cachedCondition: String? = null,
    val cachedTempMin: Double? = null,
    val cachedTempMax: Double? = null,
    val cachedHumidity: Int? = null,
    val cachedWindSpeed: Double? = null,
    val cachedIsDay: Int = 1
)
