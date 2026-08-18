package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.LocationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "world_weather_db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val defaultLocations = listOf(
                                LocationEntity(
                                    name = "Local Place",
                                    country = "Your Location",
                                    admin1 = "Auto-detect",
                                    latitude = 37.7749,
                                    longitude = -122.4194,
                                    timezone = "America/Los_Angeles",
                                    isCurrentLocation = true,
                                    isFavorite = true,
                                    orderIndex = 0
                                ),
                                LocationEntity(
                                    name = "Tokyo",
                                    country = "Japan",
                                    admin1 = "Tokyo",
                                    latitude = 35.6762,
                                    longitude = 139.6503,
                                    timezone = "Asia/Tokyo",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 1
                                ),
                                LocationEntity(
                                    name = "London",
                                    country = "United Kingdom",
                                    admin1 = "England",
                                    latitude = 51.5074,
                                    longitude = -0.1278,
                                    timezone = "Europe/London",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 2
                                ),
                                LocationEntity(
                                    name = "New York",
                                    country = "United States",
                                    admin1 = "New York",
                                    latitude = 40.7128,
                                    longitude = -74.0060,
                                    timezone = "America/New_York",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 3
                                ),
                                LocationEntity(
                                    name = "Paris",
                                    country = "France",
                                    admin1 = "Île-de-France",
                                    latitude = 48.8566,
                                    longitude = 2.3522,
                                    timezone = "Europe/Paris",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 4
                                ),
                                LocationEntity(
                                    name = "Dubai",
                                    country = "United Arab Emirates",
                                    admin1 = "Dubai",
                                    latitude = 25.2048,
                                    longitude = 55.2708,
                                    timezone = "Asia/Dubai",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 5
                                ),
                                LocationEntity(
                                    name = "Sydney",
                                    country = "Australia",
                                    admin1 = "New South Wales",
                                    latitude = -33.8688,
                                    longitude = 151.2093,
                                    timezone = "Australia/Sydney",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 6
                                ),
                                LocationEntity(
                                    name = "Singapore",
                                    country = "Singapore",
                                    admin1 = "Singapore",
                                    latitude = 1.3521,
                                    longitude = 103.8198,
                                    timezone = "Asia/Singapore",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 7
                                ),
                                LocationEntity(
                                    name = "Cairo",
                                    country = "Egypt",
                                    admin1 = "Cairo",
                                    latitude = 30.0444,
                                    longitude = 31.2357,
                                    timezone = "Africa/Cairo",
                                    isCurrentLocation = false,
                                    isFavorite = true,
                                    orderIndex = 8
                                )
                            )
                            getInstance(context).weatherDao().insertLocations(defaultLocations)
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
