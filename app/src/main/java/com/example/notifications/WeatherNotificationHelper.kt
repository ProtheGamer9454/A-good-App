package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.AlertSeverity
import com.example.data.model.WeatherAlert

object WeatherNotificationHelper {

    const val CHANNEL_ID_SEVERE_ALERTS = "severe_weather_alerts_channel"
    private const val CHANNEL_NAME = "Severe Weather & Climate Alerts"
    private const val CHANNEL_DESC = "Real-time notifications for storms, heavy rainfall, high winds, and severe weather changes."

    fun initNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_SEVERE_ALERTS, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun canPostNotifications(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    fun showWeatherAlertNotification(context: Context, alert: WeatherAlert) {
        initNotificationChannels(context)

        if (!canPostNotifications(context)) {
            Log.w("WeatherNotification", "Cannot post notification: Permission not granted")
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_LOCATION_ID", alert.locationId)
            putExtra("EXTRA_ALERT_ID", alert.id)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            alert.locationId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val priority = when (alert.severity) {
            AlertSeverity.CRITICAL -> NotificationCompat.PRIORITY_MAX
            AlertSeverity.WARNING -> NotificationCompat.PRIORITY_HIGH
            AlertSeverity.ADVISORY -> NotificationCompat.PRIORITY_DEFAULT
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_SEVERE_ALERTS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("${alert.title} - ${alert.locationName}")
            .setContentText(alert.message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle("${alert.title} • ${alert.locationName}")
                    .bigText("${alert.message}\n\n⚠️ Safety: ${alert.safetyAdvice}")
                    .setSummaryText(alert.severity.label)
            )
            .setPriority(priority)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(
                when (alert.severity) {
                    AlertSeverity.CRITICAL -> 0xFFEF4444.toInt() // Red
                    AlertSeverity.WARNING -> 0xFFF59E0B.toInt()  // Amber
                    AlertSeverity.ADVISORY -> 0xFF38BDF8.toInt() // Cyan
                }
            )
            .build()

        try {
            val notificationId = (alert.locationId.hashCode() * 31 + alert.type.hashCode())
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            Log.e("WeatherNotification", "SecurityException posting notification: ${e.message}")
        } catch (e: Exception) {
            Log.e("WeatherNotification", "Failed to post notification: ${e.message}")
        }
    }
}
