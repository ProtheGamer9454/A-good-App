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
import com.example.ui.theme.DarkBackground
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
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_glow"
    )

    val rainOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rain_fall"
    )

    val stars = remember {
        val random = Random(42)
        List(28) {
            StarParticle(
                xRatio = random.nextFloat(),
                yRatio = random.nextFloat() * 0.5f,
                radius = random.nextFloat() * 1.2f + 0.6f,
                baseAlpha = random.nextFloat() * 0.4f + 0.2f,
                blinkSpeed = random.nextInt(2000, 3500)
            )
        }
    }

    val isRainy = weatherCode in 51..67 || weatherCode in 80..82 || weatherCode in 95..99

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        val width = size.width
        val height = size.height

        // Subtle ambient spotlight glow at top for minimalism
        val glowCenter = Offset(width * 0.5f, 0f)
        val glowColor = if (isDay) Color(0x1838BDF8) else Color(0x1260A5FA)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glowColor, Color.Transparent),
                center = glowCenter,
                radius = width * 0.85f
            ),
            radius = width * 0.85f,
            center = glowCenter
        )

        // Delicate minimalist stars at night
        if (!isDay) {
            stars.forEach { star ->
                val x = star.xRatio * width
                val y = star.yRatio * height
                val dynamicAlpha = (star.baseAlpha * starGlow).coerceIn(0.05f, 0.8f)
                drawCircle(
                    color = Color.White.copy(alpha = dynamicAlpha),
                    radius = star.radius,
                    center = Offset(x, y)
                )
            }
        }

        // Clean subtle rain stream particles if rainy
        if (isRainy) {
            val numDrops = 18
            for (i in 0 until numDrops) {
                val startX = ((i * 47 + 19) % width.toInt()).toFloat()
                val speed = ((i % 4) + 3) * 0.18f
                val currentY = ((rainOffset * speed + (i * 89)) % height)
                drawLine(
                    color = Color(0x4038BDF8),
                    start = Offset(startX, currentY),
                    end = Offset(startX - 2f, currentY + 14f),
                    strokeWidth = 1f
                )
            }
        }
    }
}

fun Modifier.glassmorphicCard(
    cornerRadius: Dp = 16.dp,
    borderColor: Color = DarkCardBorder,
    backgroundColor: Color = DarkCardBg
): Modifier = composed {
    this
        .border(0.8.dp, borderColor, RoundedCornerShape(cornerRadius))
        .background(backgroundColor, RoundedCornerShape(cornerRadius))
}

