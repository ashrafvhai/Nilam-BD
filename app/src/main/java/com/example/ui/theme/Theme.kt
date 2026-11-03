package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = FbBlue,
    onPrimary = Color.White,
    primaryContainer = FbBlueLight,
    onPrimaryContainer = FbBlueDark,
    secondary = NilamGoldPrimary,
    onSecondary = Color.White,
    background = FbBgLight,
    onBackground = FbTextPrimary,
    surface = FbSurfaceLight,
    onSurface = FbTextPrimary,
    surfaceVariant = FbButtonBg,
    onSurfaceVariant = FbTextSecondary,
    outline = FbCardBorder,
    error = NilamErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = FbBlue,
    onPrimary = Color.White,
    primaryContainer = FbBlueDark,
    onPrimaryContainer = FbBlueLight,
    secondary = NilamGoldPrimary,
    onSecondary = Color.Black,
    background = FbNavyDark,
    onBackground = FbTextPrimaryDark,
    surface = FbSurfaceDark,
    onSurface = FbTextPrimaryDark,
    surfaceVariant = FbButtonDark,
    onSurfaceVariant = FbTextSecondaryDark,
    outline = FbBorderDark,
    error = NilamErrorRed,
    onError = Color.White
)

@Composable
fun NilamTheme(
    darkTheme: Boolean = false, // Default to clean Facebook Light Mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NilamTypography,
        content = content
    )
}
