package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirQualityCurrent
import com.example.data.model.CurrentWeatherUnits
import com.example.data.model.DailyForecastItem
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRose
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WeatherMetricsGrid(
    current: CurrentWeatherUnits?,
    airQuality: AirQualityCurrent?,
    todayForecast: DailyForecastItem?,
    formatTemp: (Double) -> String,
    formatWind: (Double) -> String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1: Feels Like & UV Index
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Feels Like
            MetricTile(
                modifier = Modifier.weight(1f),
                title = "Feels Like",
                icon = Icons.Default.Thermostat,
                iconTint = AccentOrange,
                value = formatTemp(current?.apparentTemperature ?: current?.temperature ?: 0.0),
                subtitle = when {
                    (current?.apparentTemperature ?: 0.0) < (current?.temperature ?: 0.0) -> "Wind makes it feel cooler"
                    (current?.apparentTemperature ?: 0.0) > (current?.temperature ?: 0.0) -> "Humidity makes it feel warmer"
                    else -> "Similar to actual temp"
                }
            )

            // UV Index
            val uv = current?.uvIndex ?: 0.0
            val uvLevel = when {
                uv <= 2 -> "Low"
                uv <= 5 -> "Moderate"
                uv <= 7 -> "High"
                uv <= 10 -> "Very High"
                else -> "Extreme"
            }
            val uvColor = when {
                uv <= 2 -> AccentEmerald
                uv <= 5 -> AccentGold
                uv <= 7 -> AccentOrange
                else -> AccentRose
            }

            MetricTile(
                modifier = Modifier.weight(1f),
                title = "UV Index",
                icon = Icons.Default.WbSunny,
                iconTint = uvColor,
                value = String.format(Locale.US, "%.1f", uv),
                subtitle = "$uvLevel exposure"
            )
        }

        // Row 2: Wind & Humidity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Wind
            val windSpeed = current?.windSpeed ?: 0.0
            val windDir = current?.windDirection ?: 0.0
            val cardinal = getCardinalDirection(windDir)

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .glassmorphicCard(12.dp),
                color = DarkCardBg
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "Wind",
                                tint = AccentCyan,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Wind",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary
                            )
                        }

                        // Compass needle icon
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Wind direction",
                            tint = AccentCyan,
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(windDir.toFloat())
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatWind(windSpeed),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "From $cardinal (${Math.round(windDir)}°)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Humidity
            val humidity = current?.relativeHumidity ?: 0
            MetricTile(
                modifier = Modifier.weight(1f),
                title = "Humidity",
                icon = Icons.Default.Water,
                iconTint = AccentSkyBlue,
                value = "$humidity%",
                subtitle = when {
                    humidity < 30 -> "Dry air"
                    humidity in 30..60 -> "Comfortable"
                    humidity in 61..80 -> "Humid"
                    else -> "Very muggy"
                }
            )
        }

        // Row 3: Pressure & Sunrise/Sunset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Pressure
            val pressure = current?.surfacePressure ?: 1013.25
            MetricTile(
                modifier = Modifier.weight(1f),
                title = "Pressure",
                icon = Icons.Default.Compress,
                iconTint = AccentPurple,
                value = "${Math.round(pressure)} hPa",
                subtitle = when {
                    pressure > 1020 -> "High pressure (Clear)"
                    pressure < 1005 -> "Low pressure (Stormy)"
                    else -> "Normal range"
                }
            )

            // Sunrise & Sunset
            val sunriseFormatted = formatSunTime(todayForecast?.sunrise ?: "")
            val sunsetFormatted = formatSunTime(todayForecast?.sunset ?: "")

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .glassmorphicCard(12.dp),
                color = DarkCardBg
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbTwilight,
                            contentDescription = "Sun Cycle",
                            tint = AccentGold,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sun Cycle",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Sunrise", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                            Text(
                                text = sunriseFormatted,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Sunset", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                            Text(
                                text = sunsetFormatted,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Air Quality Card
        if (airQuality != null) {
            AirQualityCard(airQuality = airQuality)
        }
    }
}

@Composable
fun AirQualityCard(
    airQuality: AirQualityCurrent,
    modifier: Modifier = Modifier
) {
    val aqi = airQuality.usAqi ?: airQuality.europeanAqi ?: 30
    val (status, color) = when {
        aqi <= 50 -> "Good" to AccentEmerald
        aqi <= 100 -> "Moderate" to AccentGold
        aqi <= 150 -> "Unhealthy for Sensitive" to AccentOrange
        aqi <= 200 -> "Unhealthy" to AccentRose
        else -> "Hazardous" to Color(0xFFA78BFA)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphicCard(12.dp),
        color = DarkCardBg
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "Air Quality",
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Air Quality Index (AQI)",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.2f))
                        .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelSmall,
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$aqi",
                    style = MaterialTheme.typography.displaySmall,
                    color = color,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Overall Air Quality Score",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (aqi <= 50) "Air quality is ideal for outdoor activities."
                        else "Sensitive groups should consider reducing prolonged outdoor exertion.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pollutants breakdown row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PollutantPill(name = "PM2.5", value = airQuality.pm25)
                PollutantPill(name = "PM10", value = airQuality.pm10)
                PollutantPill(name = "Ozone", value = airQuality.ozone)
                PollutantPill(name = "NO₂", value = airQuality.nitrogenDioxide)
            }
        }
    }
}

@Composable
private fun PollutantPill(name: String, value: Double?) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F1728))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name, style = MaterialTheme.typography.labelSmall, color = TextTertiary, fontSize = 10.sp)
        Text(
            text = if (value != null) "${Math.round(value)} µg" else "--",
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MetricTile(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.glassmorphicCard(12.dp),
        color = DarkCardBg
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

private fun getCardinalDirection(angle: Double): String {
    val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = (((angle + 22.5) % 360) / 45).toInt()
    return directions.getOrElse(index) { "N" }
}

private fun formatSunTime(isoTime: String): String {
    if (isoTime.isBlank()) return "--:--"
    return try {
        val dt = LocalDateTime.parse(isoTime)
        dt.format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()))
    } catch (e: Exception) {
        isoTime.substringAfter("T").take(5)
    }
}
