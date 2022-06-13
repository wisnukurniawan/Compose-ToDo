package com.wisnu.kurniawan.composetodolist.foundation.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wisnu.kurniawan.composetodolist.model.Theme

val LightColorPalette = lightColorScheme(
    primary = LightPrimary,
    primaryContainer = LightPrimary,
    secondary = LightItemBackgroundL1,
    secondaryContainer = LightItemBackgroundL2,
    background = LightBackgroundL1,
    surface = LightBackgroundL1,
    surfaceVariant = LightBackgroundL2,
    error = LightError,
    onPrimary = LightOn,
    onPrimaryContainer = LightOn,
    onSecondary = LightOn,
    onSecondaryContainer = LightOn,
    onBackground = LightOn,
    onSurface = LightOn,
    onSurfaceVariant = LightOn,
    onError = LightOn
)

val TwilightColorPalette = lightColorScheme(
    primary = TwilightPrimary,
    primaryContainer = TwilightPrimary,
    secondary = TwilightItemBackgroundL1,
    secondaryContainer = TwilightItemBackgroundL2,
    background = TwilightBackgroundL1,
    surface = TwilightBackgroundL1,
    surfaceVariant = TwilightBackgroundL2,
    error = Error,
    onPrimary = TwilightOn,
    onPrimaryContainer = TwilightOn,
    onSecondary = TwilightOn,
    onSecondaryContainer = TwilightOn,
    onBackground = TwilightOn,
    onSurface = TwilightOn,
    onSurfaceVariant = TwilightOn,
    onError = TwilightOn
)

val NightColorPalette = darkColorScheme(
    primary = NightPrimary,
    primaryContainer = NightPrimary,
    secondary = NightItemBackgroundL1,
    secondaryContainer = NightItemBackgroundL2,
    background = NightBackgroundL1,
    surface = NightBackgroundL1,
    surfaceVariant = NightBackgroundL2,
    error = Error,
    onPrimary = NightOn,
    onPrimaryContainer = NightOn,
    onSecondary = NightOn,
    onSecondaryContainer = NightOn,
    onBackground = NightOn,
    onSurface = NightOn,
    onSurfaceVariant = NightOn,
    onError = NightOn
)

val SunriseColorPalette = darkColorScheme(
    primary = SunrisePrimary,
    primaryContainer = SunrisePrimary,
    secondary = SunriseItemBackgroundL1,
    secondaryContainer = SunriseItemBackgroundL2,
    background = SunriseBackgroundL1,
    surface = SunriseBackgroundL1,
    surfaceVariant = SunriseBackgroundL2,
    error = SunriseError,
    onPrimary = SunriseOn,
    onPrimaryContainer = SunriseOn,
    onSecondary = SunriseOn,
    onSecondaryContainer = SunriseOn,
    onBackground = SunriseOn,
    onSurface = SunriseOn,
    onSurfaceVariant = SunriseOn,
    onError = SunriseOn
)

val AuroraColorPalette = darkColorScheme(
    primary = AuroraPrimary,
    primaryContainer = AuroraPrimary,
    secondary = AuroraItemBackgroundL1,
    secondaryContainer = AuroraItemBackgroundL2,
    background = AuroraBackgroundL1,
    surface = AuroraBackgroundL1,
    surfaceVariant = AuroraBackgroundL2,
    error = Error,
    onPrimary = AuroraOn,
    onPrimaryContainer = AuroraOn,
    onSecondary = AuroraOn,
    onSecondaryContainer = AuroraOn,
    onBackground = AuroraOn,
    onSurface = AuroraOn,
    onSurfaceVariant = AuroraOn,
    onError = AuroraOn
)

@SuppressLint("NewApi")
@Composable
fun Theme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        Theme.SYSTEM -> {
            if (isSystemInDarkTheme()) {
                NightColorPalette
            } else {
                LightColorPalette
            }
        }
        Theme.WALLPAPER -> {
            if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(LocalContext.current)
            } else {
                dynamicLightColorScheme(LocalContext.current)
            }
        }
        Theme.LIGHT -> LightColorPalette
        Theme.TWILIGHT -> TwilightColorPalette
        Theme.NIGHT -> NightColorPalette
        Theme.SUNRISE -> SunriseColorPalette
        Theme.AURORA -> AuroraColorPalette
    }
    val darkIcons = colors == LightColorPalette || colors == SunriseColorPalette
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = darkIcons,
            isNavigationBarContrastEnforced = false
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
