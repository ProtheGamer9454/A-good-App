package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = Color(0xFF041E28),
    primaryContainer = Color(0xFF0A3C4E),
    onPrimaryContainer = Color(0xFFB8F3FF),
    secondary = AccentSkyBlue,
    onSecondary = Color(0xFF062338),
    secondaryContainer = Color(0xFF143B5C),
    onSecondaryContainer = Color(0xFFCEEBFF),
    tertiary = AccentGold,
    onTertiary = Color(0xFF332000),
    tertiaryContainer = Color(0xFF573900),
    onTertiaryContainer = Color(0xFFFFDF9E),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    outlineVariant = Color(0xFF1E2D4A),
    error = AccentRose,
    onError = Color(0xFF3C0811)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Dark theme by default
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
