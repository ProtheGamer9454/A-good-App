package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GeoLocationResult
import com.example.ui.components.AtmosphericSkyBackground
import com.example.ui.components.glassmorphicCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.WeatherUiState
import com.example.ui.viewmodel.WeatherViewModel

data class PopularCity(
    val name: String,
    val country: String,
    val admin1: String?,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val flagEmoji: String
)

val POPULAR_WORLD_CITIES = listOf(
    PopularCity("Tokyo", "Japan", "Tokyo", 35.6762, 139.6503, "Asia/Tokyo", "🇯🇵"),
    PopularCity("London", "United Kingdom", "England", 51.5074, -0.1278, "Europe/London", "🇬🇧"),
    PopularCity("New York", "United States", "New York", 40.7128, -74.0060, "America/New_York", "🇺🇸"),
    PopularCity("Paris", "France", "Île-de-France", 48.8566, 2.3522, "Europe/Paris", "🇫🇷"),
    PopularCity("Dubai", "United Arab Emirates", "Dubai", 25.2048, 55.2708, "Asia/Dubai", "🇦🇪"),
    PopularCity("Sydney", "Australia", "New South Wales", -33.8688, 151.2093, "Australia/Sydney", "🇦🇺"),
    PopularCity("Singapore", "Singapore", "Singapore", 1.3521, 103.8198, "Asia/Singapore", "🇸🇬"),
    PopularCity("Cairo", "Egypt", "Cairo", 30.0444, 31.2357, "Africa/Cairo", "🇪🇬"),
    PopularCity("Rome", "Italy", "Lazio", 41.9028, 12.4964, "Europe/Rome", "🇮🇹"),
    PopularCity("Seoul", "South Korea", "Seoul", 37.5665, 126.9780, "Asia/Seoul", "🇰🇷"),
    PopularCity("Reykjavik", "Iceland", "Capital Region", 64.1466, -21.9426, "Atlantic/Reykjavik", "🇮🇸"),
    PopularCity("Rio de Janeiro", "Brazil", "Rio de Janeiro", -22.9068, -43.1729, "America/Sao_Paulo", "🇧🇷"),
    PopularCity("Los Angeles", "United States", "California", 34.0522, -118.2437, "America/Los_Angeles", "🇺🇸"),
    PopularCity("Mumbai", "India", "Maharashtra", 19.0760, 72.8777, "Asia/Kolkata", "🇮🇳"),
    PopularCity("Toronto", "Canada", "Ontario", 43.6532, -79.3832, "America/Toronto", "🇨🇦"),
    PopularCity("Berlin", "Germany", "Berlin", 52.5200, 13.4050, "Europe/Berlin", "🇩🇪")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchLocationsScreen(
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        AtmosphericSkyBackground(isDay = false, weatherCode = 0)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Explore Global Places",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Find weather and time across any country or city",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = {
                        Text(
                            "Search city (e.g., Tokyo, Oslo, Miami)...",
                            color = TextTertiary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AccentCyan
                        )
                    },
                    trailingIcon = {
                        if (uiState.isSearching) {
                            CircularProgressIndicator(
                                color = AccentCyan,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkCardBg,
                        unfocusedContainerColor = DarkCardBg,
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = AccentCyan
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("location_search_input")
                )
            }

            // Search Results
            if (uiState.searchResults.isNotEmpty()) {
                item {
                    Text(
                        text = "Search Results (${uiState.searchResults.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = AccentCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(uiState.searchResults) { geo ->
                    val isAlreadySaved = uiState.savedLocations.any {
                        it.latitude == geo.latitude && it.longitude == geo.longitude
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassmorphicCard(12.dp),
                        color = DarkCardBg
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = geo.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                val subtitle = listOfNotNull(geo.admin1, geo.country)
                                    .filter { it.isNotBlank() }
                                    .joinToString(", ")
                                Text(
                                    text = subtitle.ifBlank { "Coordinates: ${geo.latitude}, ${geo.longitude}" },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                if (!geo.timezone.isNullOrBlank()) {
                                    Text(
                                        text = "Time Zone: ${geo.timezone}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AccentSkyBlue
                                    )
                                }
                            }

                            if (isAlreadySaved) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x3334D399))
                                        .border(1.dp, AccentEmerald.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Added",
                                        tint = AccentEmerald,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Added",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AccentEmerald,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = { viewModel.addPlaceFromSearch(geo) },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x2238BDF8))
                                        .border(1.dp, AccentCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                        .testTag("add_search_result_${geo.name}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Place",
                                        tint = AccentCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (uiState.searchQuery.length >= 2 && !uiState.isSearching) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matching places found. Try another city name.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Popular Global Destinations Section
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "World Hubs",
                        tint = AccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Popular World Hubs & Capitals",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    POPULAR_WORLD_CITIES.forEach { city ->
                        val isAdded = uiState.savedLocations.any {
                            it.name.equals(city.name, ignoreCase = true)
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isAdded) Color(0x3334D399) else DarkCardBg)
                                .border(
                                    1.dp,
                                    if (isAdded) AccentEmerald.copy(alpha = 0.5f) else DarkCardBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    if (!isAdded) {
                                        viewModel.addPlaceFromSearch(
                                            GeoLocationResult(
                                                name = city.name,
                                                country = city.country,
                                                admin1 = city.admin1,
                                                latitude = city.lat,
                                                longitude = city.lon,
                                                timezone = city.timezone
                                            )
                                        )
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            color = Color.Transparent
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(city.flagEmoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = city.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                if (isAdded) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Added",
                                        tint = AccentEmerald,
                                        modifier = Modifier.size(13.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        tint = AccentCyan,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
