package com.example.ui.viewmodel

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.GeoLocationResult
import com.example.data.model.LocationEntity
import com.example.data.repository.LocationWeatherDetail
import com.example.data.repository.WeatherRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WeatherRepository = WeatherRepository(
        AppDatabase.getInstance(application).weatherDao()
    )

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var clockJob: Job? = null
    private var searchJob: Job? = null

    init {
        startClockTicker()
        observeLocations()
    }

    private fun startClockTicker() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            while (true) {
                _uiState.update { it.copy(currentTimeMillis = System.currentTimeMillis()) }
                delay(1000)
            }
        }
    }

    private fun observeLocations() {
        viewModelScope.launch {
            repository.allLocationsFlow.distinctUntilChanged().collect { locations ->
                val currentSelectedId = _uiState.value.selectedLocationId
                val targetSelected = if (currentSelectedId != null && locations.any { it.id == currentSelectedId }) {
                    currentSelectedId
                } else {
                    locations.firstOrNull { it.isCurrentLocation }?.id
                        ?: locations.firstOrNull()?.id
                }

                _uiState.update {
                    it.copy(
                        savedLocations = locations,
                        selectedLocationId = targetSelected
                    )
                }

                // If selected location has changed or detail is missing, load detail
                if (targetSelected != null) {
                    val targetLoc = locations.firstOrNull { it.id == targetSelected }
                    if (targetLoc != null && (_uiState.value.selectedWeatherDetail?.location?.id != targetSelected)) {
                        loadWeatherForLocation(targetLoc)
                    }
                }

                // Also background refresh all saved cities if they haven't been updated recently
                refreshAllLocationsSummary(locations)
            }
        }
    }

    fun selectTab(tab: NavigationTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun selectLocation(location: LocationEntity) {
        _uiState.update {
            it.copy(
                selectedLocationId = location.id,
                activeTab = NavigationTab.WEATHER
            )
        }
        loadWeatherForLocation(location)
    }

    fun selectLocationById(id: Long) {
        val loc = _uiState.value.savedLocations.firstOrNull { it.id == id }
        if (loc != null) {
            selectLocation(loc)
        }
    }

    fun loadWeatherForLocation(location: LocationEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.fetchDetailedWeather(location)
            result.onSuccess { detail ->
                _uiState.update { state ->
                    val updatedMap = state.cachedWeatherMap.toMutableMap()
                    updatedMap[location.id] = detail
                    state.copy(
                        selectedWeatherDetail = detail,
                        cachedWeatherMap = updatedMap,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load weather data"
                    )
                }
            }
        }
    }

    fun refreshCurrentWeather() {
        val selectedId = _uiState.value.selectedLocationId ?: return
        val loc = _uiState.value.savedLocations.firstOrNull { it.id == selectedId } ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val result = repository.fetchDetailedWeather(loc)
            result.onSuccess { detail ->
                _uiState.update { state ->
                    val updatedMap = state.cachedWeatherMap.toMutableMap()
                    updatedMap[loc.id] = detail
                    state.copy(
                        selectedWeatherDetail = detail,
                        cachedWeatherMap = updatedMap,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        errorMessage = error.message ?: "Refresh failed"
                    )
                }
            }
            refreshAllLocationsSummary(_uiState.value.savedLocations)
        }
    }

    private fun refreshAllLocationsSummary(locations: List<LocationEntity>) {
        viewModelScope.launch {
            locations.take(12).forEach { loc ->
                // Only refresh if detail is not cached or older than 10 mins
                val existing = _uiState.value.cachedWeatherMap[loc.id]
                val needsUpdate = existing == null || (System.currentTimeMillis() - existing.lastUpdatedMillis > 10 * 60 * 1000)
                if (needsUpdate) {
                    val res = repository.fetchDetailedWeather(loc)
                    res.onSuccess { detail ->
                        _uiState.update { state ->
                            val updatedMap = state.cachedWeatherMap.toMutableMap()
                            updatedMap[loc.id] = detail
                            state.copy(cachedWeatherMap = updatedMap)
                        }
                    }
                    delay(200) // slight delay to avoid bursting API
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.trim().length < 2) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(350)
            _uiState.update { it.copy(isSearching = true) }
            val results = repository.searchLocations(query)
            _uiState.update { it.copy(searchResults = results, isSearching = false) }
        }
    }

    fun addPlaceFromSearch(geo: GeoLocationResult) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingLocation = true) }
            val tz = if (!geo.timezone.isNullOrBlank()) geo.timezone else "UTC"
            val id = repository.addLocation(
                name = geo.name,
                country = geo.country ?: "",
                admin1 = geo.admin1,
                latitude = geo.latitude,
                longitude = geo.longitude,
                timezone = tz,
                isCurrent = false
            )
            _uiState.update {
                it.copy(
                    isAddingLocation = false,
                    searchQuery = "",
                    searchResults = emptyList(),
                    selectedLocationId = id,
                    activeTab = NavigationTab.WEATHER
                )
            }
        }
    }

    fun removeLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.deleteLocation(location)
            _uiState.update { state ->
                val updatedMap = state.cachedWeatherMap.toMutableMap()
                updatedMap.remove(location.id)
                state.copy(cachedWeatherMap = updatedMap)
            }
        }
    }

    fun setAsHomeLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.setAsCurrentLocation(location)
        }
    }

    fun setTimeTravelHourOffset(offset: Int) {
        _uiState.update {
            it.copy(
                timeTravelHourOffset = offset,
                isTimeTravelActive = offset != 0
            )
        }
    }

    fun resetTimeTravel() {
        _uiState.update {
            it.copy(
                timeTravelHourOffset = 0,
                isTimeTravelActive = false
            )
        }
    }

    fun setTempUnit(unit: TempUnit) {
        _uiState.update { it.copy(tempUnit = unit) }
    }

    fun setTimeFormat(format: TimeFormat) {
        _uiState.update { it.copy(timeFormat = format) }
    }

    fun setWindUnit(unit: WindUnit) {
        _uiState.update { it.copy(windUnit = unit) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun updateWithGpsLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            var cityName = "Current Location"
            var countryName = ""
            var stateName = ""

            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val address = addresses?.firstOrNull()
                if (address != null) {
                    cityName = address.locality ?: address.subAdminArea ?: address.adminArea ?: "My Place"
                    countryName = address.countryName ?: ""
                    stateName = address.adminArea ?: ""
                }
            } catch (e: Exception) {
                Log.w("WeatherViewModel", "Reverse geocoding failed: ${e.message}")
            }

            val localTz = ZoneId.systemDefault().id
            val id = repository.addLocation(
                name = cityName,
                country = countryName,
                admin1 = stateName,
                latitude = lat,
                longitude = lon,
                timezone = localTz,
                isCurrent = true
            )
            _uiState.update { it.copy(selectedLocationId = id, activeTab = NavigationTab.WEATHER) }
        }
    }

    // Conversion helpers
    fun formatTemperature(tempCelsius: Double?): String {
        if (tempCelsius == null) return "--°"
        return when (_uiState.value.tempUnit) {
            TempUnit.CELSIUS -> "${Math.round(tempCelsius)}°C"
            TempUnit.FAHRENHEIT -> {
                val f = (tempCelsius * 9 / 5) + 32
                "${Math.round(f)}°F"
            }
        }
    }

    fun formatTemperatureValueOnly(tempCelsius: Double?): String {
        if (tempCelsius == null) return "--"
        return when (_uiState.value.tempUnit) {
            TempUnit.CELSIUS -> "${Math.round(tempCelsius)}°"
            TempUnit.FAHRENHEIT -> {
                val f = (tempCelsius * 9 / 5) + 32
                "${Math.round(f)}°"
            }
        }
    }

    fun formatWindSpeed(kmh: Double?): String {
        if (kmh == null) return "--"
        return when (_uiState.value.windUnit) {
            WindUnit.KMH -> "${Math.round(kmh)} km/h"
            WindUnit.MPH -> "${Math.round(kmh * 0.621371)} mph"
            WindUnit.MS -> "${String.format(Locale.US, "%.1f", kmh / 3.6)} m/s"
        }
    }

    fun getLocalZonedDateTime(timezoneStr: String, epochMillis: Long = _uiState.value.currentTimeMillis): ZonedDateTime {
        val zoneId = try {
            if (timezoneStr.isNotBlank()) ZoneId.of(timezoneStr) else ZoneId.systemDefault()
        } catch (e: Exception) {
            ZoneId.systemDefault()
        }
        val instant = Instant.ofEpochMilli(epochMillis)
        var zdt = instant.atZone(zoneId)
        if (_uiState.value.isTimeTravelActive) {
            zdt = zdt.plusHours(_uiState.value.timeTravelHourOffset.toLong())
        }
        return zdt
    }

    fun formatFormattedTime(zdt: ZonedDateTime, includeSeconds: Boolean = false): String {
        val pattern = when (_uiState.value.timeFormat) {
            TimeFormat.FORMAT_12H -> if (includeSeconds) "hh:mm:ss a" else "hh:mm a"
            TimeFormat.FORMAT_24H -> if (includeSeconds) "HH:mm:ss" else "HH:mm"
        }
        return zdt.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
    }

    fun getTimeDifferenceString(targetTz: String): String {
        val localZone = ZoneId.systemDefault()
        val targetZone = try {
            ZoneId.of(targetTz)
        } catch (e: Exception) {
            return "Local"
        }

        val now = Instant.now()
        val localOffset = localZone.rules.getOffset(now).totalSeconds
        val targetOffset = targetZone.rules.getOffset(now).totalSeconds
        val diffHours = (targetOffset - localOffset) / 3600.0

        return when {
            diffHours == 0.0 -> "Same time as you"
            diffHours > 0 -> {
                val formatted = if (diffHours % 1 == 0.0) "${diffHours.toInt()}h" else "${String.format(Locale.US, "%.1f", diffHours)}h"
                "$formatted ahead"
            }
            else -> {
                val absDiff = -diffHours
                val formatted = if (absDiff % 1 == 0.0) "${absDiff.toInt()}h" else "${String.format(Locale.US, "%.1f", absDiff)}h"
                "$formatted behind"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        clockJob?.cancel()
        searchJob?.cancel()
    }
}
