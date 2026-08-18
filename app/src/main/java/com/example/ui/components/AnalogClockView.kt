package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentSkyBlue
import java.time.ZonedDateTime
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClockView(
    zonedDateTime: ZonedDateTime,
    isDay: Boolean = true,
    size: Dp = 120.dp,
    modifier: Modifier = Modifier
) {
    val hour = zonedDateTime.hour
    val minute = zonedDateTime.minute
    val second = zonedDateTime.second

    val hourAngle = ((hour % 12) + minute / 60f + second / 3600f) * 30f - 90f
    val minuteAngle = (minute + second / 60f) * 6f - 90f
    val secondAngle = second * 6f - 90f

    Box(
        modifier = modifier
            .size(size)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(6.dp)) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = this.size.width / 2

            // Dial background circle
            drawCircle(
                brush = Brush.radialGradient(
                    colors = if (isDay) listOf(Color(0xFF132A4A), Color(0xFF0C192E))
                    else listOf(Color(0xFF16192E), Color(0xFF090D18)),
                    center = center,
                    radius = radius
                ),
                radius = radius,
                center = center
            )

            // Outer dial rim
            drawCircle(
                color = if (isDay) AccentSkyBlue.copy(alpha = 0.3f) else AccentCyan.copy(alpha = 0.2f),
                radius = radius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Hour tick marks
            for (i in 0 until 12) {
                val angle = Math.toRadians((i * 30 - 90).toDouble())
                val isMajor = i % 3 == 0
                val tickInnerRadius = radius - (if (isMajor) 10.dp.toPx() else 6.dp.toPx())
                val tickOuterRadius = radius - 3.dp.toPx()

                val startX = (center.x + tickInnerRadius * cos(angle)).toFloat()
                val startY = (center.y + tickInnerRadius * sin(angle)).toFloat()
                val endX = (center.x + tickOuterRadius * cos(angle)).toFloat()
                val endY = (center.y + tickOuterRadius * sin(angle)).toFloat()

                drawLine(
                    color = if (isMajor) (if (isDay) AccentGold else AccentCyan) else Color.White.copy(alpha = 0.3f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Hour Hand
            val hourRad = Math.toRadians(hourAngle.toDouble())
            val hourLength = radius * 0.52f
            val hourEnd = Offset(
                (center.x + hourLength * cos(hourRad)).toFloat(),
                (center.y + hourLength * sin(hourRad)).toFloat()
            )
            drawLine(
                color = Color.White,
                start = center,
                end = hourEnd,
                strokeWidth = 3.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Minute Hand
            val minRad = Math.toRadians(minuteAngle.toDouble())
            val minLength = radius * 0.75f
            val minEnd = Offset(
                (center.x + minLength * cos(minRad)).toFloat(),
                (center.y + minLength * sin(minRad)).toFloat()
            )
            drawLine(
                color = AccentCyan,
                start = center,
                end = minEnd,
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Second Hand
            val secRad = Math.toRadians(secondAngle.toDouble())
            val secLength = radius * 0.85f
            val secEnd = Offset(
                (center.x + secLength * cos(secRad)).toFloat(),
                (center.y + secLength * sin(secRad)).toFloat()
            )
            drawLine(
                color = if (isDay) AccentGold else AccentSkyBlue,
                start = center,
                end = secEnd,
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Center Pin
            drawCircle(
                color = if (isDay) AccentGold else AccentCyan,
                radius = 4.dp.toPx(),
                center = center
            )
            drawCircle(
                color = Color(0xFF090D18),
                radius = 2.dp.toPx(),
                center = center
            )
        }
    }
}
