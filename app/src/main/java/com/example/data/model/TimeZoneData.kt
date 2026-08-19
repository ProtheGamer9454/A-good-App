package com.example.data.model

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class WorldTimeZoneItem(
    val zoneId: String,
    val region: String,
    val cityName: String,
    val offsetString: String,
    val offsetSeconds: Int,
    val formattedTime: String,
    val formattedDate: String,
    val isDay: Boolean,
    val diffFromUser: String,
    val approxLat: Double,
    val approxLon: Double
)

object WorldTimeZoneRepository {

    val REGIONS = listOf(
        "All Zones",
        "Americas",
        "Europe",
        "Asia",
        "Africa",
        "Oceania",
        "Pacific",
        "Atlantic",
        "Indian",
        "UTC / GMT"
    )

    fun getAllTimeZones(currentTimeMillis: Long, is24Hour: Boolean = false): List<WorldTimeZoneItem> {
        val userZone = ZoneId.systemDefault()
        val nowInstant = Instant.ofEpochMilli(currentTimeMillis)
        val userOffset = try { userZone.rules.getOffset(nowInstant).totalSeconds } catch (e: Exception) { 0 }

        val timePattern = if (is24Hour) "HH:mm" else "hh:mm a"
        val timeFormatter = DateTimeFormatter.ofPattern(timePattern, Locale.US)
        val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.US)

        return ZoneId.getAvailableZoneIds()
            .filter { id ->
                !id.startsWith("SystemV/") && !id.startsWith("Mideast/") && !id.startsWith("US/") && !id.startsWith("Canada/") && !id.startsWith("Brazil/")
            }
            .sorted()
            .mapNotNull { zoneStr ->
                try {
                    val zone = ZoneId.of(zoneStr)
                    val zdt = nowInstant.atZone(zone)
                    val offset = zone.rules.getOffset(nowInstant)
                    val totalSec = offset.totalSeconds
                    val offsetHours = totalSec / 3600
                    val offsetMins = Math.abs((totalSec % 3600) / 60)
                    val sign = if (totalSec >= 0) "+" else "-"
                    val offsetStr = "UTC$sign${String.format(Locale.US, "%02d:%02d", Math.abs(offsetHours), offsetMins)}"

                    val diffHours = (totalSec - userOffset) / 3600.0
                    val diffStr = when {
                        diffHours == 0.0 -> "Local (Same as you)"
                        diffHours > 0 -> {
                            val f = if (diffHours % 1 == 0.0) "${diffHours.toInt()}h" else "${String.format(Locale.US, "%.1f", diffHours)}h"
                            "$f ahead"
                        }
                        else -> {
                            val abs = -diffHours
                            val f = if (abs % 1 == 0.0) "${abs.toInt()}h" else "${String.format(Locale.US, "%.1f", abs)}h"
                            "$f behind"
                        }
                    }

                    val hour = zdt.hour
                    val isDay = hour in 6..19

                    val parts = zoneStr.split("/")
                    val region = when {
                        zoneStr.startsWith("America/") -> "Americas"
                        zoneStr.startsWith("Europe/") -> "Europe"
                        zoneStr.startsWith("Asia/") -> "Asia"
                        zoneStr.startsWith("Africa/") -> "Africa"
                        zoneStr.startsWith("Australia/") -> "Oceania"
                        zoneStr.startsWith("Pacific/") -> "Pacific"
                        zoneStr.startsWith("Atlantic/") -> "Atlantic"
                        zoneStr.startsWith("Indian/") -> "Indian"
                        zoneStr.startsWith("Antarctica/") -> "Antarctica"
                        zoneStr.startsWith("Etc/") || zoneStr == "UTC" || zoneStr == "GMT" -> "UTC / GMT"
                        else -> "Global"
                    }

                    val city = parts.last().replace("_", " ")

                    // Approximate center coordinate for weather lookup preview
                    val (lat, lon) = getApproxCoordsForZone(zoneStr)

                    WorldTimeZoneItem(
                        zoneId = zoneStr,
                        region = region,
                        cityName = city,
                        offsetString = offsetStr,
                        offsetSeconds = totalSec,
                        formattedTime = zdt.format(timeFormatter),
                        formattedDate = zdt.format(dateFormatter),
                        isDay = isDay,
                        diffFromUser = diffStr,
                        approxLat = lat,
                        approxLon = lon
                    )
                } catch (e: Exception) {
                    null
                }
            }
    }

    private fun getApproxCoordsForZone(zoneStr: String): Pair<Double, Double> {
        return when {
            zoneStr == "America/New_York" -> Pair(40.7128, -74.0060)
            zoneStr == "America/Los_Angeles" -> Pair(34.0522, -118.2437)
            zoneStr == "America/Chicago" -> Pair(41.8781, -87.6298)
            zoneStr == "America/Denver" -> Pair(39.7392, -104.9903)
            zoneStr == "America/Toronto" -> Pair(43.6532, -79.3832)
            zoneStr == "America/Vancouver" -> Pair(49.2827, -123.1207)
            zoneStr == "America/Sao_Paulo" -> Pair(-23.5505, -46.6333)
            zoneStr == "America/Buenos_Aires" -> Pair(-34.6037, -58.3816)
            zoneStr == "America/Mexico_City" -> Pair(19.4326, -99.1332)
            zoneStr == "Europe/London" -> Pair(51.5074, -0.1278)
            zoneStr == "Europe/Paris" -> Pair(48.8566, 2.3522)
            zoneStr == "Europe/Berlin" -> Pair(52.5200, 13.4050)
            zoneStr == "Europe/Rome" -> Pair(41.9028, 12.4964)
            zoneStr == "Europe/Madrid" -> Pair(40.4168, -3.7038)
            zoneStr == "Europe/Athens" -> Pair(37.9838, 23.7275)
            zoneStr == "Europe/Moscow" -> Pair(55.7558, 37.6173)
            zoneStr == "Asia/Tokyo" -> Pair(35.6762, 139.6503)
            zoneStr == "Asia/Kolkata" -> Pair(28.6139, 77.2090)
            zoneStr == "Asia/Shanghai" -> Pair(31.2304, 121.4737)
            zoneStr == "Asia/Singapore" -> Pair(1.3521, 103.8198)
            zoneStr == "Asia/Dubai" -> Pair(25.2048, 55.2708)
            zoneStr == "Asia/Seoul" -> Pair(37.5665, 126.9780)
            zoneStr == "Asia/Bangkok" -> Pair(13.7563, 100.5018)
            zoneStr == "Asia/Hong_Kong" -> Pair(22.3193, 114.1694)
            zoneStr == "Africa/Cairo" -> Pair(30.0444, 31.2357)
            zoneStr == "Africa/Johannesburg" -> Pair(-26.2041, 28.0473)
            zoneStr == "Africa/Lagos" -> Pair(6.5244, 3.3792)
            zoneStr == "Africa/Nairobi" -> Pair(-1.2921, 36.8219)
            zoneStr == "Australia/Sydney" -> Pair(-33.8688, 151.2093)
            zoneStr == "Australia/Melbourne" -> Pair(-37.8136, 144.9631)
            zoneStr == "Australia/Perth" -> Pair(-31.9505, 115.8605)
            zoneStr == "Pacific/Auckland" -> Pair(-36.8485, 174.7633)
            zoneStr == "Pacific/Honolulu" -> Pair(21.3069, -157.8583)
            else -> Pair(0.0, 0.0)
        }
    }
}
