package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun WorldTimeSlider(
    offsetHours: Int,
    isTimeTravelActive: Boolean,
    onOffsetChanged: (Int) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphicCard(16.dp),
        color = DarkCardBg
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AvTimer,
                        contentDescription = "Time Converter",
                        tint = TextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TIME SCRUBBER",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }

                if (isTimeTravelActive) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x18FFFFFF))
                            .border(0.6.dp, Color(0x40FFFFFF), RoundedCornerShape(6.dp))
                            .clickable { onReset() }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset",
                            tint = TextPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Reset",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isTimeTravelActive) {
                    val sign = if (offsetHours > 0) "+$offsetHours" else "$offsetHours"
                    "Simulating $sign hours across all locations"
                } else {
                    "Scrub to preview time across all cities"
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (isTimeTravelActive) AccentGold else TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Slider(
                value = offsetHours.toFloat(),
                onValueChange = { onOffsetChanged(Math.round(it)) },
                valueRange = -12f..12f,
                steps = 23,
                colors = SliderDefaults.colors(
                    thumbColor = TextPrimary,
                    activeTrackColor = Color.White.copy(alpha = 0.8f),
                    inactiveTrackColor = Color(0xFF1E1E26),
                    activeTickColor = Color.White.copy(alpha = 0.2f),
                    inactiveTickColor = Color.White.copy(alpha = 0.1f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("-12h", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                Text(
                    text = if (offsetHours == 0) "Live (Now)" else if (offsetHours > 0) "+$offsetHours hrs" else "$offsetHours hrs",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (offsetHours == 0) TextPrimary else AccentGold,
                    fontWeight = FontWeight.SemiBold
                )
                Text("+12h", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
            }
        }
    }
}

