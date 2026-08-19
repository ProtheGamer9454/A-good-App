package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocationEntity
import com.example.data.model.WeatherCodeUtil
import com.example.ui.components.AnalogClockView
import com.example.ui.components.AtmosphericSkyBackground
import com.example.ui.components.DailyForecastList
import com.example.ui.components.DigitalTimeView
import com.example.ui.components.HourlyForecastRow
import com.example.ui.components.WeatherMetricsGrid
import com.example.ui.components.glassmorphicCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.WeatherUiState
import com.example.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherDetailScreen(
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    onNavigateToSearch: () -> Unit,
    onRequestGpsLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val detail = uiState.selectedWeatherDetail
    val currentLocation = uiState.savedLocations.firstOrNull { it.id == uiState.selectedLocationId }
        ?: uiState.savedLocations.firstOrNull()

    val currentUnits = detail?.weatherResponse?.current
    val weatherCode = currentUnits?.weatherCode ?: currentLocation?.cachedWeatherCode ?: 0
    val isDay = (currentUnits?.isDay ?: currentLocation?.cachedIsDay ?: 1) == 1

    val zonedDateTime = viewModel.getLocalZonedDateTime(
        currentLocation?.timezone ?: "UTC",
        uiState.currentTimeMillis
    )
    val timeDiff = viewModel.getTimeDifferenceString(currentLocation?.timezone ?: "UTC")

    var showAnalogClock by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // Atmospheric Sky Background
        AtmosphericSkyBackground(
            isDay = isDay,
            weatherCode = weatherCode
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar: Location Chips Carousel & Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Location selection row
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(end = 6.dp)
                    ) {
                        items(uiState.savedLocations) { loc ->
                            val isSelected = loc.id == uiState.selectedLocationId
                            val chipBg = if (isSelected) Color(0x22FFFFFF) else Color(0x0DFFFFFF)
                            val chipBorder = if (isSelected) Color(0x60FFFFFF) else DarkCardBorder

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(chipBg)
                                    .border(0.8.dp, chipBorder, RoundedCornerShape(20.dp))
                                    .clickable { viewModel.selectLocation(loc) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                if (loc.isCurrentLocation) {
                                    Icon(
                                        imageVector = Icons.Default.MyLocation,
                                        contentDescription = "Current Location",
                                        tint = if (isSelected) AccentCyan else TextTertiary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                                Text(
                                    text = loc.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Action buttons with fixed spacing to prevent overlap
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // GPS detect button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x14FFFFFF))
                                .border(0.8.dp, DarkCardBorder, CircleShape)
                                .clickable { onRequestGpsLocation() }
                                .testTag("gps_location_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Detect Location",
                                tint = TextPrimary,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Add city button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0x14FFFFFF))
                                .border(0.8.dp, DarkCardBorder, CircleShape)
                                .clickable { onNavigateToSearch() }
                                .testTag("add_city_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Location",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Error banner if any
            if (uiState.errorMessage != null) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassmorphicCard(12.dp, borderColor = Color(0x33F87171), backgroundColor = Color(0x18F87171)),
                        color = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = Color(0xFFF87171),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.errorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.refreshCurrentWeather() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Retry",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Clean Minimalist Hero Weather Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassmorphicCard(
                            cornerRadius = 20.dp,
                            borderColor = DarkCardBorder,
                            backgroundColor = DarkCardBg
                        ),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Location Title & Refresh
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentLocation?.name ?: "Unknown Place",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                val region = listOfNotNull(currentLocation?.admin1, currentLocation?.country)
                                    .filter { it.isNotBlank() }
                                    .joinToString(", ")
                                if (region.isNotBlank()) {
                                    Text(
                                        text = region,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.refreshCurrentWeather() },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x0DFFFFFF))
                                    .testTag("refresh_weather_button")
                            ) {
                                if (uiState.isRefreshing || uiState.isLoading) {
                                    CircularProgressIndicator(
                                        color = TextPrimary,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(14.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Large Minimal Temperature & Weather Emoji
                        val tempVal = currentUnits?.temperature ?: currentLocation?.cachedTemp
                        val conditionDesc = currentUnits?.let { WeatherCodeUtil.getCondition(it.weatherCode) }
                            ?: currentLocation?.cachedCondition
                            ?: "Clear"
                        val emoji = WeatherCodeUtil.getIconEmoji(weatherCode, isDay)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 44.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = viewModel.formatTemperatureValueOnly(tempVal),
                                fontSize = 68.sp,
                                lineHeight = 72.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Light,
                                letterSpacing = (-2).sp
                            )
                            Text(
                                text = uiState.tempUnit.symbol,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextSecondary,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(bottom = 28.dp)
                            )
                        }

                        Text(
                            text = conditionDesc,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // High / Low / Feels like clean row
                        val minTemp = detail?.dailyForecasts?.firstOrNull()?.tempMinC ?: currentLocation?.cachedTempMin
                        val maxTemp = detail?.dailyForecasts?.firstOrNull()?.tempMaxC ?: currentLocation?.cachedTempMax
                        val feelsLike = currentUnits?.apparentTemperature

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (minTemp != null && maxTemp != null) {
                                Text(
                                    text = "H: ${viewModel.formatTemperature(maxTemp)}  •  L: ${viewModel.formatTemperature(minTemp)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                            if (feelsLike != null) {
                                Text(
                                    text = "•",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextTertiary
                                )
                                Text(
                                    text = "Feels ${viewModel.formatTemperature(feelsLike)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Minimal Timezone / Local Time Chip
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x0AFFFFFF))
                                .border(0.8.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                                .clickable { showAnalogClock = !showAnalogClock }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (showAnalogClock) {
                                AnalogClockView(
                                    zonedDateTime = zonedDateTime,
                                    isDay = isDay,
                                    size = 90.dp
                                )
                            } else {
                                DigitalTimeView(
                                    zonedDateTime = zonedDateTime,
                                    timeFormat = uiState.timeFormat,
                                    timeDifferenceString = timeDiff
                                )
                            }
                        }
                    }
                }
            }

            // Hourly Forecast
            item {
                HourlyForecastRow(
                    hourlyList = detail?.hourlyForecasts ?: emptyList(),
                    formatTemp = { viewModel.formatTemperature(it) }
                )
            }

            // 7-Day Daily Forecast
            item {
                DailyForecastList(
                    dailyList = detail?.dailyForecasts ?: emptyList(),
                    formatTemp = { viewModel.formatTemperature(it) }
                )
            }

            // Detailed Metric Cards (UV, Wind Compass, Humidity, Pressure, Sun, AQI)
            item {
                WeatherMetricsGrid(
                    current = currentUnits,
                    airQuality = detail?.airQuality,
                    todayForecast = detail?.dailyForecasts?.firstOrNull(),
                    formatTemp = { viewModel.formatTemperature(it) },
                    formatWind = { viewModel.formatWindSpeed(it) }
                )
            }
        }
    }
}
