package com.vluk4.itunescodechallenge.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppDarkColorScheme = darkColorScheme(
    primary = TextPrimary,
    onPrimary = Black,
    background = Black,
    onBackground = TextPrimary,
    surface = Black,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = SurfaceVariantDark,
    surfaceContainerHigh = SheetSurface,
    surfaceContainerHighest = SheetSurface,
    outline = OutlineDark,
    outlineVariant = OutlineDark,
)

@Composable
fun ITunesCodeChallengeTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
