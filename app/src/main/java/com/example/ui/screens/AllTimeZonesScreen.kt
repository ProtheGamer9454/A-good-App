package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorldTimeZoneItem
import com.example.data.model.WorldTimeZoneRepository
import com.example.ui.components.glassmorphicCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.TimeFormat
import com.example.ui.viewmodel.WeatherUiState
import com.example.ui.viewmodel.WeatherViewModel

@Composable
fun AllTimeZonesScreen(
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val is24H = uiState.timeFormat == TimeFormat.FORMAT_24H

    val allZones = remember(uiState.currentTimeMillis, is24H) {
        WorldTimeZoneRepository.getAllTimeZones(uiState.currentTimeMillis, is24H)
    }

    val filteredZones = remember(allZones, uiState.selectedTimeZoneRegion, uiState.timeZoneSearchQuery) {
        allZones.filter { item ->
            val matchRegion = uiState.selectedTimeZoneRegion == "All Zones" || item.region == uiState.selectedTimeZoneRegion
            val matchSearch = uiState.timeZoneSearchQuery.isBlank() ||
                    item.zoneId.contains(uiState.timeZoneSearchQuery, ignoreCase = true) ||
                    item.cityName.contains(uiState.timeZoneSearchQuery, ignoreCase = true) ||
                    item.region.contains(uiState.timeZoneSearchQuery, ignoreCase = true) ||
                    item.offsetString.contains(uiState.timeZoneSearchQuery, ignoreCase = true)
            matchRegion && matchSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "All Time Zones",
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "All World Time Zones",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                }
                Text(
                    text = "Live clocks and offsets across all standard IANA time zones",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .glassmorphicCard(6.dp, DarkCardBorder, DarkSurface)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${filteredZones.size} zones",
                    color = AccentCyan,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Region Filter Pills
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(vertical = 2.dp)
        ) {
            items(WorldTimeZoneRepository.REGIONS) { region ->
                val isSelected = uiState.selectedTimeZoneRegion == region
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setSelectedTimeZoneRegion(region) },
                    label = {
                        Text(
                            text = region,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentCyan.copy(alpha = 0.18f),
                        selectedLabelColor = AccentCyan,
                        containerColor = DarkSurface,
                        labelColor = TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) AccentCyan.copy(alpha = 0.6f) else DarkCardBorder
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar for Time Zones
        OutlinedTextField(
            value = uiState.timeZoneSearchQuery,
            onValueChange = { viewModel.setTimeZoneSearchQuery(it) },
            placeholder = {
                Text("Search any city, continent or timezone (e.g. Tokyo, Cairo, UTC+5)...", color = TextTertiary, fontSize = 11.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Time Zone",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            },
            trailingIcon = {
                if (uiState.timeZoneSearchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.setTimeZoneSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("timezone_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Time Zones List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredZones, key = { it.zoneId }) { zoneItem ->
                TimeZoneCard(
                    item = zoneItem,
                    onOpenWeather = { viewModel.openTimeZoneInWeatherView(zoneItem) },
                    onPinToClocks = { viewModel.addTimeZoneToSavedClocks(zoneItem) }
                )
            }
        }
    }
}

@Composable
private fun TimeZoneCard(
    item: WorldTimeZoneItem,
    onOpenWeather: () -> Unit,
    onPinToClocks: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .glassmorphicCard(cornerRadius = 10.dp, borderColor = DarkCardBorder, backgroundColor = DarkCardBg)
            .clickable { onOpenWeather() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("timezone_card_${item.zoneId}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Column: City & Region & UTC
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.cityName,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(DarkSurface, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = item.offsetString,
                        color = AccentSkyBlue,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${item.region} • ${item.zoneId}",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "(${item.diffFromUser})",
                    color = TextTertiary,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Right Column: Digital Clock & Weather / Pin Action
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (item.isDay) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (item.isDay) "Day" else "Night",
                        tint = if (item.isDay) AccentGold else AccentPurple,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.formattedTime,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = item.formattedDate,
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { onPinToClocks() },
                modifier = Modifier
                    .size(28.dp)
                    .glassmorphicCard(6.dp, DarkCardBorder, DarkSurface)
                    .testTag("pin_tz_${item.zoneId}")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pin to World Clocks",
                    tint = AccentCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
