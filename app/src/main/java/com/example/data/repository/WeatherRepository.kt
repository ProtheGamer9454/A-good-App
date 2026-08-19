package com.example.data.repository

import android.util.Log
import com.example.data.local.WeatherDao
import com.example.data.model.AirQualityCurrent
import com.example.data.model.DailyForecastItem
import com.example.data.model.GeoLocationResult
import com.example.data.model.HourlyForecastItem
import com.example.data.model.LocationEntity
import com.example.data.model.WeatherCodeUtil
import com.example.data.model.WeatherResponse
import com.example.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class LocationWeatherDetail(
    val location: LocationEntity,
    val weatherResponse: WeatherResponse,
    val airQuality: AirQualityCurrent?,
    val hourlyForecasts: List<HourlyForecastItem>,
    val dailyForecasts: List<DailyForecastItem>,
    val lastUpdatedMillis: Long = System.currentTimeMillis()
)

class WeatherRepository(private val weatherDao: WeatherDao) {

    val allLocationsFlow: Flow<List<LocationEntity>> = weatherDao.getAllLocationsFlow()

    suspend fun getAllLocations(): List<LocationEntity> = withContext(Dispatchers.IO) {
        weatherDao.getAllLocations()
    }

    suspend fun getLocationById(id: Long): LocationEntity? = withContext(Dispatchers.IO) {
        weatherDao.getLocationById(id)
    }

    suspend fun fetchDetailedWeather(
        location: LocationEntity
    ): Result<LocationWeatherDetail> = withContext(Dispatchers.IO) {
        try {
            val tzParam = if (location.timezone.isNotBlank()) location.timezone else "auto"
            val weatherResp = ApiClient.weatherApi.getForecast(
                latitude = location.latitude,
                longitude = location.longitude,
                timezone = tzParam
            )

            val airQualityResp = try {
                ApiClient.airQualityApi.getAirQuality(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timezone = tzParam
                )
            } catch (e: Exception) {
                Log.w("WeatherRepository", "Air quality fetch failed: ${e.message}")
                null
            }

            // Update cache in database
            val current = weatherResp.current
            if (current != null) {
                val temp = current.temperature
                val code = current.weatherCode
                val condition = WeatherCodeUtil.getCondition(code)
                val daily = weatherResp.daily
                val minTemp = daily?.temperatureMin?.firstOrNull()
                val maxTemp = daily?.temperatureMax?.firstOrNull()

                val updatedLocation = location.copy(
                    lastUpdated = System.currentTimeMillis(),
                    cachedTemp = temp,
                    cachedWeatherCode = code,
                    cachedCondition = condition,
                    cachedTempMin = minTemp,
                    cachedTempMax = maxTemp,
                    cachedHumidity = current.relativeHumidity,
                    cachedWindSpeed = current.windSpeed,
                    cachedIsDay = current.isDay
                )
                weatherDao.updateLocation(updatedLocation)
            }

            // Parse Hourly
            val hourlyList = mutableListOf<HourlyForecastItem>()
            val hourly = weatherResp.hourly
            if (hourly != null) {
                val size = minOf(
                    hourly.time.size,
                    hourly.temperature.size,
                    hourly.weatherCode.size
                )
                val formatter = DateTimeFormatter.ofPattern("h a", Locale.getDefault())
                val limit = minOf(size, 48) // next 48 hours

                for (i in 0 until limit) {
                    val timeStr = hourly.time.getOrNull(i) ?: continue
                    val temp = hourly.temperature.getOrNull(i) ?: 0.0
                    val code = hourly.weatherCode.getOrNull(i) ?: 0
                    val pop = hourly.precipitationProbability.getOrNull(i) ?: 0
                    val isDay = (hourly.isDay.getOrNull(i) ?: 1) == 1
                    val uv = hourly.uvIndex.getOrNull(i) ?: 0.0
                    val wind = hourly.windSpeed.getOrNull(i) ?: 0.0

                    val displayHour = try {
                        val dt = LocalDateTime.parse(timeStr)
                        dt.format(formatter)
                    } catch (e: Exception) {
                        timeStr.substringAfter("T").take(5)
                    }

                    hourlyList.add(
                        HourlyForecastItem(
                            timeIso = timeStr,
                            displayHour = displayHour,
                            tempC = temp,
                            weatherCode = code,
                            precipitationProb = pop,
                            isDay = isDay,
                            uvIndex = uv,
                            windSpeed = wind
                        )
                    )
                }
            }

            // Parse Daily
            val dailyList = mutableListOf<DailyForecastItem>()
            val daily = weatherResp.daily
            if (daily != null) {
                val size = minOf(
                    daily.time.size,
                    daily.temperatureMax.size,
                    daily.temperatureMin.size
                )
                for (i in 0 until size) {
                    val dateStr = daily.time.getOrNull(i) ?: continue
                    val code = daily.weatherCode.getOrNull(i) ?: 0
                    val maxT = daily.temperatureMax.getOrNull(i) ?: 0.0
                    val minT = daily.temperatureMin.getOrNull(i) ?: 0.0
                    val pop = daily.precipitationProbabilityMax.getOrNull(i) ?: 0
                    val uv = daily.uvIndexMax.getOrNull(i) ?: 0.0
                    val sunrise = daily.sunrise.getOrNull(i) ?: ""
                    val sunset = daily.sunset.getOrNull(i) ?: ""
                    val windMax = daily.windSpeedMax.getOrNull(i) ?: 0.0

                    val dayOfWeek = try {
                        val d = LocalDate.parse(dateStr)
                        if (i == 0) "Today"
                        else if (i == 1) "Tomorrow"
                        else d.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())
                    } catch (e: Exception) {
                        "Day ${i + 1}"
                    }

                    dailyList.add(
                        DailyForecastItem(
                            dateIso = dateStr,
                            dayOfWeek = dayOfWeek,
                            weatherCode = code,
                            tempMaxC = maxT,
                            tempMinC = minT,
                            precipitationProb = pop,
                            uvIndexMax = uv,
                            sunrise = sunrise,
                            sunset = sunset,
                            windSpeedMax = windMax
                        )
                    )
                }
            }

            Result.success(
                LocationWeatherDetail(
                    location = location,
                    weatherResponse = weatherResp,
                    airQuality = airQualityResp?.current,
                    hourlyForecasts = hourlyList,
                    dailyForecasts = dailyList
                )
            )
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Failed to fetch weather", e)
            Result.failure(e)
        }
    }

    suspend fun searchLocations(query: String): List<GeoLocationResult> = withContext(Dispatchers.IO) {
        if (query.trim().length < 2) return@withContext emptyList()
        try {
            val response = ApiClient.geocodingApi.searchLocations(name = query.trim())
            response.results ?: emptyList()
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Failed to search locations: ${e.message}")
            emptyList()
        }
    }

    suspend fun addLocation(
        name: String,
        country: String,
        admin1: String?,
        latitude: Double,
        longitude: Double,
        timezone: String,
        isCurrent: Boolean = false
    ): Long = withContext(Dispatchers.IO) {
        if (isCurrent) {
            weatherDao.clearCurrentLocationFlags()
        }
        val count = weatherDao.getLocationCount()
        val entity = LocationEntity(
            name = name,
            country = country,
            admin1 = admin1,
            latitude = latitude,
            longitude = longitude,
            timezone = timezone,
            isCurrentLocation = isCurrent,
            isFavorite = true,
            orderIndex = if (isCurrent) 0 else count + 1
        )
        weatherDao.insertLocation(entity)
    }

    suspend fun updateLocation(location: LocationEntity) = withContext(Dispatchers.IO) {
        weatherDao.updateLocation(location)
    }

    suspend fun deleteLocation(location: LocationEntity) = withContext(Dispatchers.IO) {
        weatherDao.deleteLocation(location)
    }

    suspend fun setAsCurrentLocation(location: LocationEntity) = withContext(Dispatchers.IO) {
        weatherDao.clearCurrentLocationFlags()
        weatherDao.updateLocation(location.copy(isCurrentLocation = true))
    }

    suspend fun fetchQuickWeather(lat: Double, lon: Double, timezone: String): QuickWeatherSnapshot? = withContext(Dispatchers.IO) {
        try {
            val tzParam = if (timezone.isNotBlank()) timezone else "auto"
            val resp = ApiClient.weatherApi.getForecast(latitude = lat, longitude = lon, timezone = tzParam)
            val current = resp.current ?: return@withContext null
            val code = current.weatherCode
            val condition = WeatherCodeUtil.getCondition(code)
            val daily = resp.daily
            val minTemp = daily?.temperatureMin?.firstOrNull()
            val maxTemp = daily?.temperatureMax?.firstOrNull()
            QuickWeatherSnapshot(
                tempCelsius = current.temperature ?: 0.0,
                weatherCode = code,
                condition = condition,
                isDay = current.isDay == 1,
                humidity = current.relativeHumidity ?: 0,
                windSpeed = current.windSpeed ?: 0.0,
                minTemp = minTemp,
                maxTemp = maxTemp
            )
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Quick weather fetch failed for ($lat,$lon): ${e.message}")
            null
        }
    }
}

data class QuickWeatherSnapshot(
    val tempCelsius: Double,
    val weatherCode: Int,
    val condition: String,
    val isDay: Boolean,
    val humidity: Int,
    val windSpeed: Double,
    val minTemp: Double?,
    val maxTemp: Double?
)
