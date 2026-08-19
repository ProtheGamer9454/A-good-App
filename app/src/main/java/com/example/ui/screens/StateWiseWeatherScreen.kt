package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CountryInfo
import com.example.data.model.StateInfo
import com.example.data.model.WeatherCodeUtil
import com.example.data.model.WorldCountriesDatabase
import com.example.data.repository.QuickWeatherSnapshot
import com.example.ui.components.glassmorphicCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardBorderHigh
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.WeatherUiState
import com.example.ui.viewmodel.WeatherViewModel

@Composable
fun StateWiseWeatherScreen(
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCountry = remember(uiState.selectedCountryId) {
        WorldCountriesDatabase.COUNTRIES.firstOrNull { it.id == uiState.selectedCountryId }
            ?: WorldCountriesDatabase.COUNTRIES.first()
    }

    LaunchedEffect(selectedCountry.id) {
        viewModel.loadStateWeatherForCountry(selectedCountry)
    }

    val filteredCountries = remember(uiState.selectedContinent, uiState.countrySearchQuery) {
        WorldCountriesDatabase.COUNTRIES.filter { country ->
            val matchContinent = uiState.selectedContinent == "All Continents" || country.continent == uiState.selectedContinent
            val matchSearch = uiState.countrySearchQuery.isBlank() || country.name.contains(uiState.countrySearchQuery, ignoreCase = true) || country.code.contains(uiState.countrySearchQuery, ignoreCase = true)
            matchContinent && matchSearch
        }
    }

    val filteredStates = remember(selectedCountry, uiState.stateSearchQuery) {
        if (uiState.stateSearchQuery.isBlank()) {
            selectedCountry.states
        } else {
            selectedCountry.states.filter {
                it.name.contains(uiState.stateSearchQuery, ignoreCase = true) ||
                it.capitalOrHub.contains(uiState.stateSearchQuery, ignoreCase = true) ||
                (it.code != null && it.code.contains(uiState.stateSearchQuery, ignoreCase = true))
            }
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
                        imageVector = Icons.Default.Public,
                        contentDescription = "Global States",
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Country & State Weather",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                }
                Text(
                    text = "Real-time state & province weather for every country",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            IconButton(
                onClick = { viewModel.loadStateWeatherForCountry(selectedCountry) },
                modifier = Modifier
                    .size(34.dp)
                    .glassmorphicCard(8.dp, DarkCardBorder, DarkSurface)
                    .testTag("refresh_state_weather_btn")
            ) {
                if (uiState.isLoadingStateWeather) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = AccentCyan
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh States Weather",
                        tint = AccentCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Continent Filter Pills
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(vertical = 2.dp)
        ) {
            items(WorldCountriesDatabase.CONTINENTS) { continent ->
                val isSelected = uiState.selectedContinent == continent
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectContinent(continent) },
                    label = {
                        Text(
                            text = continent,
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

        Spacer(modifier = Modifier.height(8.dp))

        // Country Selector Horizontal Ribbon
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredCountries) { country ->
                val isSelected = country.id == selectedCountry.id
                Box(
                    modifier = Modifier
                        .glassmorphicCard(
                            cornerRadius = 10.dp,
                            borderColor = if (isSelected) AccentCyan else DarkCardBorder,
                            backgroundColor = if (isSelected) AccentCyan.copy(alpha = 0.12f) else DarkCardBg
                        )
                        .clickable { viewModel.selectCountry(country.id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("country_tab_${country.code}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = country.flagEmoji, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = country.name,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            Text(
                                text = "${country.states.size} states",
                                color = if (isSelected) AccentCyan else TextTertiary,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // State Search Bar
        OutlinedTextField(
            value = uiState.stateSearchQuery,
            onValueChange = { viewModel.setStateSearchQuery(it) },
            placeholder = {
                Text("Search ${selectedCountry.name} states or capitals...", color = TextTertiary, fontSize = 12.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search State",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            },
            trailingIcon = {
                if (uiState.stateSearchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.setStateSearchQuery("") }) {
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
                .testTag("state_search_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Active Country Summary Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphicCard(8.dp, DarkCardBorder, DarkSurface)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = selectedCountry.flagEmoji, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${selectedCountry.name} • Capital: ${selectedCountry.capital}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "${filteredStates.size} regions",
                color = AccentCyan,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // State Weather Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredStates, key = { "${selectedCountry.code}_${it.name}" }) { state ->
                val cacheKey = "${selectedCountry.code}_${state.name}"
                val weather = uiState.stateWeatherCache[cacheKey]

                StateWeatherCard(
                    state = state,
                    countryName = selectedCountry.name,
                    weather = weather,
                    currentTimeMillis = uiState.currentTimeMillis,
                    formatTemp = { viewModel.formatTemperature(it) },
                    onOpenDetail = { viewModel.openStateInWeatherView(state, selectedCountry.name) }
                )
            }
        }
    }
}

@Composable
private fun StateWeatherCard(
    state: StateInfo,
    countryName: String,
    weather: QuickWeatherSnapshot?,
    currentTimeMillis: Long,
    formatTemp: (Double?) -> String,
    onOpenDetail: () -> Unit
) {
    val zdt = try {
        java.time.Instant.ofEpochMilli(currentTimeMillis)
            .atZone(java.time.ZoneId.of(state.timezone))
    } catch (e: Exception) {
        java.time.Instant.ofEpochMilli(currentTimeMillis).atZone(java.time.ZoneId.systemDefault())
    }
    val localTime = zdt.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.US))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .glassmorphicCard(cornerRadius = 10.dp, borderColor = DarkCardBorder, backgroundColor = DarkCardBg)
            .clickable { onOpenDetail() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("state_card_${state.name}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Column: State name & capital & local time
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = state.name,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (state.code != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(DarkSurface, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = state.code,
                            color = AccentSkyBlue,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Hub: ${state.capitalOrHub}",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = " • $localTime",
                    color = TextTertiary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Right Column: Weather telemetry & Temperature
        if (weather != null) {
            val weatherIcon = WeatherCodeUtil.getWeatherIcon(weather.weatherCode, weather.isDay)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatTemp(weather.tempCelsius),
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = weatherIcon,
                            contentDescription = weather.condition,
                            tint = if (weather.isDay) AccentGold else AccentSkyBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = weather.condition,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View forecast",
                    tint = TextTertiary,
                    modifier = Modifier.size(14.dp)
                )
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 1.5.dp,
                    color = AccentCyan.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Loading...",
                    color = TextTertiary,
                    fontSize = 10.sp
                )
            }
        }
    }
}
