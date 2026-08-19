package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.TimeFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DigitalTimeView(
    zonedDateTime: ZonedDateTime,
    timeFormat: TimeFormat,
    timeDifferenceString: String,
    modifier: Modifier = Modifier
) {
    val isDay = zonedDateTime.hour in 6..18
    val pattern = when (timeFormat) {
        TimeFormat.FORMAT_12H -> "hh:mm"
        TimeFormat.FORMAT_24H -> "HH:mm"
    }
    val timeDigits = zonedDateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
    val amPm = if (timeFormat == TimeFormat.FORMAT_12H) {
        zonedDateTime.format(DateTimeFormatter.ofPattern("a", Locale.getDefault()))
    } else ""

    val dateFormatted = zonedDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault()))
    val seconds = String.format(Locale.getDefault(), ":%02d", zonedDateTime.second)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Day/Night and difference pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(Color(0x10FFFFFF))
                .border(0.6.dp, DarkCardBorder, RoundedCornerShape(100.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = if (isDay) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = if (isDay) "Day" else "Night",
                tint = TextSecondary,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isDay) "Daytime" else "Night",
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " • $timeDifferenceString",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time display
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = timeDigits,
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = seconds,
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
            )
            if (amPm.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = amPm,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }

        Text(
            text = dateFormatted,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

