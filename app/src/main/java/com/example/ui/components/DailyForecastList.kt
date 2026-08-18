package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyForecastItem
import com.example.data.model.WeatherCodeUtil
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentSkyBlue
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun DailyForecastList(
    dailyList: List<DailyForecastItem>,
    formatTemp: (Double) -> String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphicCard(14.dp),
        color = DarkCardBg
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "7-Day Forecast",
                    tint = AccentCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "7-Day Forecast",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (dailyList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading daily forecast...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextTertiary
                    )
                }
            } else {
                val overallMin = dailyList.minOfOrNull { it.tempMinC } ?: 0.0
                val overallMax = dailyList.maxOfOrNull { it.tempMaxC } ?: 40.0
                val tempSpan = (overallMax - overallMin).coerceAtLeast(1.0)

                dailyList.forEachIndexed { index, day ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Day name
                        Text(
                            text = day.dayOfWeek,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (index == 0) AccentCyan else TextPrimary,
                            fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.width(80.dp)
                        )

                        // Condition & Rain chance
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(68.dp)
                        ) {
                            Text(
                                text = WeatherCodeUtil.getIconEmoji(day.weatherCode, isDay = true),
                                fontSize = 18.sp
                            )
                            if (day.precipitationProb > 15) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = "Rain",
                                        tint = AccentSkyBlue,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = "${day.precipitationProb}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AccentSkyBlue,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        // Min Temp
                        Text(
                            text = formatTemp(day.tempMinC),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            modifier = Modifier.width(40.dp)
                        )

                        // Visual Temp range bar
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(5.dp)
                                .padding(horizontal = 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF1B2A47))
                        ) {
                            val startFraction = ((day.tempMinC - overallMin) / tempSpan).toFloat().coerceIn(0f, 1f)
                            val endFraction = ((day.tempMaxC - overallMin) / tempSpan).toFloat().coerceIn(0f, 1f)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(endFraction)
                                    .height(5.dp)
                                    .padding(start = (startFraction * 70).dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(AccentSkyBlue, AccentGold)
                                        )
                                    )
                            )
                        }

                        // Max Temp
                        Text(
                            text = formatTemp(day.tempMaxC),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(40.dp)
                        )
                    }

                    if (index < dailyList.lastIndex) {
                        HorizontalDivider(
                            color = DarkCardBorder.copy(alpha = 0.5f),
                            thickness = 0.8.dp
                        )
                    }
                }
            }
        }
    }
}
