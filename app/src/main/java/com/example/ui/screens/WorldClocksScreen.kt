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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.WorldTimeSlider
import com.example.ui.components.glassmorphicCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.TimeFormat
import com.example.ui.viewmodel.WeatherUiState
import com.example.ui.viewmodel.WeatherViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WorldClocksScreen(
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        AtmosphericSkyBackground(isDay = false, weatherCode = 0)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "World Clocks",
                            tint = AccentCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "World Clocks",
                                style = MaterialTheme.typography.headlineSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${uiState.savedLocations.size} time zones active",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onNavigateToSearch,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x2238BDF8))
                            .border(1.dp, AccentCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .testTag("add_world_clock_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add World Clock",
                            tint = AccentCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Interactive World Time Scrubber
            item {
                WorldTimeSlider(
                    offsetHours = uiState.timeTravelHourOffset,
                    isTimeTravelActive = uiState.isTimeTravelActive,
                    onOffsetChanged = { viewModel.setTimeTravelHourOffset(it) },
                    onReset = { viewModel.resetTimeTravel() }
                )
            }

            // List of World City Clocks
            items(uiState.savedLocations, key = { it.id }) { loc ->
                WorldClockCard(
                    location = loc,
                    uiState = uiState,
                    viewModel = viewModel,
                    onClick = { viewModel.selectLocation(loc) },
                    onSetHome = { viewModel.setAsHomeLocation(loc) },
                    onDelete = { viewModel.removeLocation(loc) }
                )
            }
        }
    }
}

@Composable
fun WorldClockCard(
    location: LocationEntity,
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    onClick: () -> Unit,
    onSetHome: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zonedDateTime = viewModel.getLocalZonedDateTime(
        location.timezone,
        uiState.currentTimeMillis
    )
    val isDay = zonedDateTime.hour in 6..18
    val timeDiff = viewModel.getTimeDifferenceString(location.timezone)

    val pattern = when (uiState.timeFormat) {
        TimeFormat.FORMAT_12H -> "hh:mm"
        TimeFormat.FORMAT_24H -> "HH:mm"
    }
    val timeDigits = zonedDateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
    val amPm = if (uiState.timeFormat == TimeFormat.FORMAT_12H) {
        zonedDateTime.format(DateTimeFormatter.ofPattern("a", Locale.getDefault()))
    } else ""
    val dayOfWeek = zonedDateTime.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault()))

    val cachedDetail = uiState.cachedWeatherMap[location.id]
    val temp = cachedDetail?.weatherResponse?.current?.temperature ?: location.cachedTemp
    val code = cachedDetail?.weatherResponse?.current?.weatherCode ?: location.cachedWeatherCode ?: 0
    val emoji = WeatherCodeUtil.getIconEmoji(code, isDay)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphicCard(
                cornerRadius = 12.dp,
                borderColor = if (location.isCurrentLocation) AccentCyan.copy(alpha = 0.6f) else DarkCardBorder,
                backgroundColor = if (location.isCurrentLocation) Color(0xFF11213D) else DarkCardBg
            )
            .clickable { onClick() }
            .testTag("world_clock_card_${location.name}"),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Analog Dial Mini
            AnalogClockView(
                zonedDateTime = zonedDateTime,
                isDay = isDay,
                size = 48.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Middle: Location Name, Country, Time Difference
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (location.isCurrentLocation) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Home",
                            tint = AccentCyan,
                            modifier = Modifier
                                .size(13.dp)
                                .padding(end = 3.dp)
                        )
                    }
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = location.country.ifBlank { location.timezone },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDay) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDay) "Day" else "Night",
                        tint = if (isDay) AccentGold else AccentSkyBlue,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$dayOfWeek • $timeDiff",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDay) AccentGold else AccentSkyBlue,
                        fontSize = 11.sp
                    )
                }
            }

            // Right: Weather & Digital Time
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = timeDigits,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.ExtraBold
                    )
                    if (amPm.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = amPm,
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentGold,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Weather badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x33000000))
                        .border(0.8.dp, DarkCardBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = emoji, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = viewModel.formatTemperature(temp),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
