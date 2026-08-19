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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
        Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = this.size.width / 2

            // Dial background circle
            drawCircle(
                color = Color(0x0FFFFFFF),
                radius = radius,
                center = center
            )

            // Outer dial rim
            drawCircle(
                color = Color(0x20FFFFFF),
                radius = radius,
                center = center,
                style = Stroke(width = 0.8.dp.toPx())
            )

            // Hour tick marks (minimal 4 main ticks or 12 dots)
            for (i in 0 until 12) {
                val angle = Math.toRadians((i * 30 - 90).toDouble())
                val isMajor = i % 3 == 0
                val tickInnerRadius = radius - (if (isMajor) 6.dp.toPx() else 3.dp.toPx())
                val tickOuterRadius = radius - 1.5.dp.toPx()

                val startX = (center.x + tickInnerRadius * cos(angle)).toFloat()
                val startY = (center.y + tickInnerRadius * sin(angle)).toFloat()
                val endX = (center.x + tickOuterRadius * cos(angle)).toFloat()
                val endY = (center.y + tickOuterRadius * sin(angle)).toFloat()

                drawLine(
                    color = if (isMajor) Color.White.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.2f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isMajor) 1.5.dp.toPx() else 0.8.dp.toPx(),
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
                color = Color.White.copy(alpha = 0.9f),
                start = center,
                end = hourEnd,
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Minute Hand
            val minRad = Math.toRadians(minuteAngle.toDouble())
            val minLength = radius * 0.74f
            val minEnd = Offset(
                (center.x + minLength * cos(minRad)).toFloat(),
                (center.y + minLength * sin(minRad)).toFloat()
            )
            drawLine(
                color = Color.White.copy(alpha = 0.8f),
                start = center,
                end = minEnd,
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Second Hand
            val secRad = Math.toRadians(secondAngle.toDouble())
            val secLength = radius * 0.82f
            val secEnd = Offset(
                (center.x + secLength * cos(secRad)).toFloat(),
                (center.y + secLength * sin(secRad)).toFloat()
            )
            drawLine(
                color = if (isDay) Color(0xFFF59E0B) else Color(0xFF38BDF8),
                start = center,
                end = secEnd,
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Center Pin
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = center
            )
        }
    }
}

