package com.example.ui.viewmodel

import com.example.data.model.GeoLocationResult
import com.example.data.model.LocationEntity
import com.example.data.repository.LocationWeatherDetail

enum class TempUnit(val symbol: String, val label: String) {
    CELSIUS("°C", "Celsius"),
    FAHRENHEIT("°F", "Fahrenheit")
}

enum class TimeFormat(val label: String) {
    FORMAT_12H("12-Hour (AM/PM)"),
    FORMAT_24H("24-Hour (Military)")
}

enum class WindUnit(val symbol: String, val label: String) {
    KMH("km/h", "Kilometers per hour"),
    MPH("mph", "Miles per hour"),
    MS("m/s", "Meters per second")
}

enum class NavigationTab(val title: String) {
    WEATHER("Weather"),
    WORLD_CLOCKS("World Clocks"),
    TIME_CONVERTER("Time Converter"),
    SEARCH("Add Places"),
    SETTINGS("Settings")
}

data class WeatherUiState(
    val savedLocations: List<LocationEntity> = emptyList(),
    val selectedLocationId: Long? = null,
    val selectedWeatherDetail: LocationWeatherDetail? = null,
    val cachedWeatherMap: Map<Long, LocationWeatherDetail> = emptyMap(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    
    // Time & World Clock state
    val currentTimeMillis: Long = System.currentTimeMillis(),
    val timeTravelHourOffset: Int = 0, // -12 to +12 or 0-23
    val isTimeTravelActive: Boolean = false,
    
    // Search state
    val searchQuery: String = "",
    val searchResults: List<GeoLocationResult> = emptyList(),
    val isSearching: Boolean = false,
    val isAddingLocation: Boolean = false,

    // Settings
    val tempUnit: TempUnit = TempUnit.CELSIUS,
    val timeFormat: TimeFormat = TimeFormat.FORMAT_12H,
    val windUnit: WindUnit = WindUnit.KMH,
    val activeTab: NavigationTab = NavigationTab.WEATHER
)
