package com.example.data.model

import com.example.data.repository.LocationWeatherDetail
import java.util.UUID

enum class AlertSeverity(val label: String, val level: Int) {
    CRITICAL("Severe Warning", 3),
    WARNING("Weather Alert", 2),
    ADVISORY("Weather Advisory", 1)
}

enum class AlertType {
    THUNDERSTORM,
    HEAVY_RAIN_FLOOD,
    HEAVY_SNOW_ICE,
    EXTREME_WIND,
    EXTREME_HEAT,
    EXTREME_COLD,
    HIGH_UV,
    RAPID_TEMP_CHANGE,
    CUSTOM_TEST
}

data class WeatherAlert(
    val id: String = UUID.randomUUID().toString(),
    val locationId: Long,
    val locationName: String,
    val severity: AlertSeverity,
    val type: AlertType,
    val title: String,
    val message: String,
    val safetyAdvice: String,
    val timestampMillis: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isActive: Boolean = true
)

object SevereWeatherDetector {

    fun analyzeWeather(
        location: LocationEntity,
        detail: LocationWeatherDetail,
        previousTemp: Double? = null
    ): List<WeatherAlert> {
        val alerts = mutableListOf<WeatherAlert>()
        val current = detail.weatherResponse.current ?: return alerts
        val locName = location.name
        val code = current.weatherCode
        val temp = current.temperature ?: 0.0
        val windSpeed = current.windSpeed ?: 0.0
        val uv = current.uvIndex ?: 0.0

        // 1. Thunderstorm Detection (Codes 95, 96, 99)
        when (code) {
            99 -> alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.CRITICAL,
                    type = AlertType.THUNDERSTORM,
                    title = "⛈️ Severe Thunderstorm with Hail",
                    message = "Violent thunderstorm with heavy hail detected in $locName. Intense lightning and wind gusts.",
                    safetyAdvice = "Seek indoor shelter immediately. Stay away from windows and electrical appliances."
                )
            )
            95, 96 -> alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.WARNING,
                    type = AlertType.THUNDERSTORM,
                    title = "⛈️ Thunderstorm Warning",
                    message = "Active thunderstorm with lightning and rain detected in $locName.",
                    safetyAdvice = "Avoid open areas, tall trees, and metal structures. Stay indoors if possible."
                )
            )
        }

        // 2. Heavy Rain / Torrential Showers (Codes 65, 82)
        if (code == 65 || code == 82) {
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.WARNING,
                    type = AlertType.HEAVY_RAIN_FLOOD,
                    title = "🌧️ Heavy Downpour & Flash Flood Risk",
                    message = "Intense torrential rainfall detected in $locName. High localized water accumulation.",
                    safetyAdvice = "Drive with extreme caution. Avoid low-lying flooded roadways and drainage canals."
                )
            )
        }

        // 3. Freezing Rain / Heavy Snow / Ice Hazard (Codes 56, 57, 66, 67, 75, 86)
        if (code in listOf(56, 57, 66, 67)) {
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.CRITICAL,
                    type = AlertType.HEAVY_SNOW_ICE,
                    title = "🧊 Freezing Rain & Black Ice Hazard",
                    message = "Freezing precipitation forming severe ice layers on roads and walkways in $locName.",
                    safetyAdvice = "Roadways may be impassable with black ice. Minimize travel and walk carefully."
                )
            )
        } else if (code == 75 || code == 86) {
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.WARNING,
                    type = AlertType.HEAVY_SNOW_ICE,
                    title = "❄️ Heavy Snowfall Warning",
                    message = "Substantial heavy snow accumulation and low visibility in $locName.",
                    safetyAdvice = "Keep warm clothing and emergency kits handy. Watch for slick driving conditions."
                )
            )
        }

        // 4. Extreme High Wind / Gale (> 50 km/h or ~31 mph)
        if (windSpeed >= 50.0) {
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = if (windSpeed >= 75.0) AlertSeverity.CRITICAL else AlertSeverity.WARNING,
                    type = AlertType.EXTREME_WIND,
                    title = "💨 High Wind & Gale Warning (${Math.round(windSpeed)} km/h)",
                    message = "Damaging wind gusts of ${Math.round(windSpeed)} km/h active in $locName.",
                    safetyAdvice = "Secure loose outdoor objects. Watch for falling tree limbs and flying debris."
                )
            )
        }

        // 5. Extreme Heatwave (> 38°C / ~100.4°F)
        if (temp >= 38.0) {
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.WARNING,
                    type = AlertType.EXTREME_HEAT,
                    title = "🔥 Extreme Heat Advisory (${Math.round(temp)}°C)",
                    message = "Dangerous high temperatures exceeding 38°C detected in $locName.",
                    safetyAdvice = "Stay well hydrated. Avoid strenuous outdoor activities during peak afternoon hours."
                )
            )
        }

        // 6. Extreme Deep Freeze (< -12°C / ~10°F)
        if (temp <= -12.0) {
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.WARNING,
                    type = AlertType.EXTREME_COLD,
                    title = "🥶 Severe Freeze Warning (${Math.round(temp)}°C)",
                    message = "Frigid sub-zero temperatures dangerous for frostbite in $locName.",
                    safetyAdvice = "Dress in multiple thermal layers and protect exposed skin from frostbite."
                )
            )
        }

        // 7. Dangerous UV Index (>= 9.0)
        if (uv >= 9.0) {
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.ADVISORY,
                    type = AlertType.HIGH_UV,
                    title = "☀️ Very High UV Radiation (Index: ${String.format(java.util.Locale.US, "%.1f", uv)})",
                    message = "Intense solar radiation index detected in $locName.",
                    safetyAdvice = "Wear SPF 50+ sunscreen, UV-rated sunglasses, and wide-brim protective headwear."
                )
            )
        }

        // 8. Rapid Temperature Shift (e.g. drop or surge >= 8°C compared to previous cached reading)
        if (previousTemp != null && Math.abs(temp - previousTemp) >= 8.0) {
            val shiftDirection = if (temp > previousTemp) "surge" else "drop"
            val diff = Math.round(Math.abs(temp - previousTemp))
            alerts.add(
                WeatherAlert(
                    locationId = location.id,
                    locationName = locName,
                    severity = AlertSeverity.ADVISORY,
                    type = AlertType.RAPID_TEMP_CHANGE,
                    title = "🌡️ Rapid Temperature $shiftDirection (${diff}°C Shift)",
                    message = "Sudden atmospheric thermal swing from ${Math.round(previousTemp)}°C to ${Math.round(temp)}°C in $locName.",
                    safetyAdvice = "Rapid weather fronts may bring erratic wind and precipitation changes."
                )
            )
        }

        return alerts
    }
}
