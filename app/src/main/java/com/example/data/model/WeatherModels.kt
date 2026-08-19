package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.FilterDrama
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezone: String? = null,
    @Json(name = "timezone_abbreviation") val timezoneAbbreviation: String? = null,
    @Json(name = "elevation") val elevation: Double? = null,
    val current: CurrentWeatherUnits? = null,
    val hourly: HourlyWeatherData? = null,
    val daily: DailyWeatherData? = null
)

@JsonClass(generateAdapter = true)
data class CurrentWeatherUnits(
    val time: String? = null,
    @Json(name = "temperature_2m") val temperature: Double? = null,
    @Json(name = "relative_humidity_2m") val relativeHumidity: Int? = null,
    @Json(name = "apparent_temperature") val apparentTemperature: Double? = null,
    @Json(name = "is_day") val isDay: Int = 1,
    val precipitation: Double? = null,
    @Json(name = "weather_code") val weatherCode: Int = 0,
    @Json(name = "surface_pressure") val surfacePressure: Double? = null,
    @Json(name = "wind_speed_10m") val windSpeed: Double? = null,
    @Json(name = "wind_direction_10m") val windDirection: Double? = null,
    @Json(name = "uv_index") val uvIndex: Double? = null
)

@JsonClass(generateAdapter = true)
data class HourlyWeatherData(
    val time: List<String> = emptyList(),
    @Json(name = "temperature_2m") val temperature: List<Double> = emptyList(),
    @Json(name = "relative_humidity_2m") val relativeHumidity: List<Int> = emptyList(),
    @Json(name = "precipitation_probability") val precipitationProbability: List<Int> = emptyList(),
    @Json(name = "weather_code") val weatherCode: List<Int> = emptyList(),
    @Json(name = "surface_pressure") val surfacePressure: List<Double> = emptyList(),
    @Json(name = "wind_speed_10m") val windSpeed: List<Double> = emptyList(),
    @Json(name = "uv_index") val uvIndex: List<Double> = emptyList(),
    @Json(name = "is_day") val isDay: List<Int> = emptyList()
)

@JsonClass(generateAdapter = true)
data class DailyWeatherData(
    val time: List<String> = emptyList(),
    @Json(name = "weather_code") val weatherCode: List<Int> = emptyList(),
    @Json(name = "temperature_2m_max") val temperatureMax: List<Double> = emptyList(),
    @Json(name = "temperature_2m_min") val temperatureMin: List<Double> = emptyList(),
    val sunrise: List<String> = emptyList(),
    val sunset: List<String> = emptyList(),
    @Json(name = "uv_index_max") val uvIndexMax: List<Double> = emptyList(),
    @Json(name = "precipitation_sum") val precipitationSum: List<Double> = emptyList(),
    @Json(name = "precipitation_probability_max") val precipitationProbabilityMax: List<Int> = emptyList(),
    @Json(name = "wind_speed_10m_max") val windSpeedMax: List<Double> = emptyList()
)

@JsonClass(generateAdapter = true)
data class AirQualityResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val current: AirQualityCurrent? = null
)

@JsonClass(generateAdapter = true)
data class AirQualityCurrent(
    val time: String? = null,
    @Json(name = "european_aqi") val europeanAqi: Int? = null,
    @Json(name = "us_aqi") val usAqi: Int? = null,
    @Json(name = "pm10") val pm10: Double? = null,
    @Json(name = "pm2_5") val pm25: Double? = null,
    @Json(name = "carbon_monoxide") val carbonMonoxide: Double? = null,
    @Json(name = "nitrogen_dioxide") val nitrogenDioxide: Double? = null,
    @Json(name = "ozone") val ozone: Double? = null
)

data class HourlyForecastItem(
    val timeIso: String,
    val displayHour: String,
    val tempC: Double,
    val weatherCode: Int,
    val precipitationProb: Int,
    val isDay: Boolean,
    val uvIndex: Double,
    val windSpeed: Double
)

data class DailyForecastItem(
    val dateIso: String,
    val dayOfWeek: String,
    val weatherCode: Int,
    val tempMaxC: Double,
    val tempMinC: Double,
    val precipitationProb: Int,
    val uvIndexMax: Double,
    val sunrise: String,
    val sunset: String,
    val windSpeedMax: Double
)

object WeatherCodeUtil {
    fun getCondition(code: Int): String = when (code) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45 -> "Fog"
        48 -> "Depositing rime fog"
        51 -> "Light drizzle"
        53 -> "Moderate drizzle"
        55 -> "Dense drizzle"
        56 -> "Freezing drizzle"
        57 -> "Heavy freezing drizzle"
        61 -> "Slight rain"
        63 -> "Moderate rain"
        65 -> "Heavy rain"
        66 -> "Freezing rain"
        67 -> "Heavy freezing rain"
        71 -> "Slight snow fall"
        73 -> "Moderate snow fall"
        75 -> "Heavy snow fall"
        77 -> "Snow grains"
        80 -> "Slight rain showers"
        81 -> "Moderate rain showers"
        82 -> "Violent rain showers"
        85 -> "Slight snow showers"
        86 -> "Heavy snow showers"
        95 -> "Thunderstorm"
        96 -> "Thunderstorm with slight hail"
        99 -> "Thunderstorm with heavy hail"
        else -> "Clear"
    }

    fun getIconEmoji(code: Int, isDay: Boolean = true): String = when (code) {
        0 -> if (isDay) "☀️" else "🌙"
        1 -> if (isDay) "🌤️" else "🌑"
        2 -> if (isDay) "⛅" else "☁️"
        3 -> "☁️"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌦️"
        56, 57 -> "🌨️"
        61, 63 -> "🌧️"
        65 -> "🌧️"
        66, 67 -> "🧊"
        71, 73, 75, 77 -> "❄️"
        80, 81, 82 -> "🌧️"
        85, 86 -> "🌨️"
        95, 96, 99 -> "⛈️"
        else -> if (isDay) "☀️" else "🌙"
    }

    fun getWeatherIcon(code: Int, isDay: Boolean = true): ImageVector = when (code) {
        0 -> if (isDay) Icons.Default.WbSunny else Icons.Default.NightsStay
        1 -> if (isDay) Icons.Default.WbSunny else Icons.Default.NightsStay
        2 -> if (isDay) Icons.Default.WbCloudy else Icons.Default.NightsStay
        3 -> Icons.Default.Cloud
        45, 48 -> Icons.Default.FilterDrama
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> Icons.Default.WaterDrop
        56, 57, 66, 67, 71, 73, 75, 77, 85, 86 -> Icons.Default.AcUnit
        95, 96, 99 -> Icons.Default.Thunderstorm
        else -> if (isDay) Icons.Default.WbSunny else Icons.Default.NightsStay
    }

    fun isPrecipitating(code: Int): Boolean {
        return code in 51..67 || code in 80..82 || code in 95..99
    }

    fun isSnowing(code: Int): Boolean {
        return code in 71..77 || code in 85..86
    }
}
