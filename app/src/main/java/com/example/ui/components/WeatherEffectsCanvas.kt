package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardBorder
import kotlin.random.Random

data class StarParticle(
    val xRatio: Float,
    val yRatio: Float,
    val radius: Float,
    val baseAlpha: Float,
    val blinkSpeed: Int
)

@Composable
fun AtmosphericSkyBackground(
    isDay: Boolean,
    weatherCode: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sky_anim")
    val starGlow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_glow"
    )

    val rainOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rain_fall"
    )

    val stars = remember {
        val random = Random(42)
        List(45) {
            StarParticle(
                xRatio = random.nextFloat(),
                yRatio = random.nextFloat() * 0.7f,
                radius = random.nextFloat() * 1.8f + 0.8f,
                baseAlpha = random.nextFloat() * 0.5f + 0.3f,
                blinkSpeed = random.nextInt(1500, 3000)
            )
        }
    }

    val isRainy = weatherCode in 51..67 || weatherCode in 80..82 || weatherCode in 95..99
    val isSnowy = weatherCode in 71..77 || weatherCode in 85..86

    // Atmospheric Gradient Background
    val backgroundBrush = remember(isDay, weatherCode) {
        when {
            !isDay -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF070B14),
                    Color(0xFF0D1627),
                    Color(0xFF111E36)
                )
            )
            isRainy -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0F172A),
                    Color(0xFF1E293B),
                    Color(0xFF172554)
                )
            )
            isSnowy -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0E1A2D),
                    Color(0xFF1E293B),
                    Color(0xFF1E3A5F)
                )
            )
            else -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0C192E),
                    Color(0xFF132D52),
                    Color(0xFF0B1B33)
                )
            )
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        val width = size.width
        val height = size.height

        // Draw ambient celestial glow at top
        val glowCenter = if (isDay) Offset(width * 0.75f, height * 0.12f) else Offset(width * 0.8f, height * 0.15f)
        val glowColor = if (isDay) Color(0x33FFB703) else Color(0x2238BDF8)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glowColor, Color.Transparent),
                center = glowCenter,
                radius = width * 0.6f
            ),
            radius = width * 0.6f,
            center = glowCenter
        )

        // Draw stars at night
        if (!isDay) {
            stars.forEach { star ->
                val x = star.xRatio * width
                val y = star.yRatio * height
                val dynamicAlpha = (star.baseAlpha * starGlow).coerceIn(0.1f, 1f)
                drawCircle(
                    color = Color.White.copy(alpha = dynamicAlpha),
                    radius = star.radius,
                    center = Offset(x, y)
                )
            }
        }

        // Draw subtle rain stream particles if rainy
        if (isRainy) {
            val numDrops = 30
            for (i in 0 until numDrops) {
                val startX = ((i * 37 + 13) % width.toInt()).toFloat()
                val speed = ((i % 5) + 3) * 0.2f
                val currentY = ((rainOffset * speed + (i * 73)) % height)
                drawLine(
                    color = Color(0x6638BDF8),
                    start = Offset(startX, currentY),
                    end = Offset(startX - 4f, currentY + 18f),
                    strokeWidth = 1.5f
                )
            }
        }
    }
}

fun Modifier.glassmorphicCard(
    cornerRadius: Dp = 14.dp,
    borderColor: Color = DarkCardBorder,
    backgroundColor: Color = DarkCardBg
): Modifier = composed {
    this
        .border(1.dp, borderColor, RoundedCornerShape(cornerRadius))
        .background(backgroundColor, RoundedCornerShape(cornerRadius))
}
