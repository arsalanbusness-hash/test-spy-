package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SpyDarkColorScheme = darkColorScheme(
    primary = NeonAccent,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0369A1),
    onPrimaryContainer = Color.White,
    secondary = SpyCrimson,
    onSecondary = Color.White,
    secondaryContainer = SpyCrimsonDark,
    onSecondaryContainer = Color.White,
    tertiary = InnocentEmerald,
    onTertiary = Color.Black,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCardSurface,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder
)

@Composable
fun SpyGameTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SpyDarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    SpyGameTheme(content = content)
}
