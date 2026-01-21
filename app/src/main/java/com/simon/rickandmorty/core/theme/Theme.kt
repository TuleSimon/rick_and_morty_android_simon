package com.simon.rickandmorty.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RickGreenDark,
    onPrimary = Color.Black,
    secondary = RickBlueDark,
    onSecondary = Color.Black,
    tertiary = MortyYellowDark,
    onTertiary = Color.Black,
    background = SpaceBlack,
    surface = DeepSpaceGrey,
    onBackground = Color.White,
    onSurface = Color.White,
    error = ErrorRedLight,
    surfaceContainer = SpaceGreyLight,
    onSurfaceVariant = Color(0xFFBDBDBD)
)

private val LightColorScheme = lightColorScheme(
    primary = RickGreenPrimary,
    onPrimary = Color.White,
    secondary = RickBlueSecondary,
    onSecondary = Color.White,
    tertiary = MortyYellowTertiary,
    onTertiary = Color.Black,
    background = LabCoatWhite,
    surface = Color.White,
    onBackground = PortalDark,
    onSurface = PortalDark,
    error = ErrorRed,
    surfaceContainer = Color(0xFFFBFBFB),
    surfaceContainerLow = Color(0xFFFBFBFB),
    onSurfaceVariant = Color(0xFF424242)
)

@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}