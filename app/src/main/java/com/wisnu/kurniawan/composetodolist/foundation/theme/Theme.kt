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
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    secondary = LightSecondary,
    secondaryContainer = LightSecondaryVariant,
    onSecondary = Color.Black,
    onSecondaryContainer = Color.Black,
    background = LightBackground,
    onBackground = Color.Black,
    surface = LightBackground,
    onSurface = Color.Black,
    error = LightError,
    onError = Color.White
)

val TwilightColorPalette = lightColorScheme(
    primary = TwilightPrimary,
    primaryContainer = TwilightPrimary,
    onPrimary = TwilightOn,
    onPrimaryContainer = TwilightOn,
    secondary = TwilightSecondary,
    secondaryContainer = TwilightSecondaryVariant,
    onSecondary = TwilightOn,
    onSecondaryContainer = TwilightOn,
    background = TwilightBackground,
    onBackground = TwilightOn,
    surface = TwilightBackground,
    onSurface = TwilightOn,
    error = Error,
    onError = Color.White
)

val NightColorPalette = darkColorScheme(
    primary = NightPrimary,
    primaryContainer = NightPrimary,
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    secondary = NightSecondary,
    secondaryContainer = NightSecondaryVariant,
    onSecondary = Color.White,
    onSecondaryContainer = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    error = Error,
    onError = Color.White
)

val SunriseColorPalette = darkColorScheme(
    primary = SunrisePrimary,
    primaryContainer = SunrisePrimary,
    onPrimary = SunriseOn,
    onPrimaryContainer = SunriseOn,
    secondary = SunriseSecondary,
    secondaryContainer = SunriseSecondaryVariant,
    onSecondary = SunriseOn,
    onSecondaryContainer = SunriseOn,
    background = SunriseBackground,
    onBackground = SunriseOn,
    surface = SunriseBackground,
    onSurface = SunriseOn,
    error = SunriseError,
    onError = Color.White
)

val AuroraColorPalette = darkColorScheme(
    primary = AuroraPrimary,
    primaryContainer = AuroraPrimary,
    onPrimary = AuroraOn,
    onPrimaryContainer = AuroraOn,
    secondary = AuroraSecondary,
    secondaryContainer = AuroraSecondaryVariant,
    onSecondary = AuroraOn,
    onSecondaryContainer = AuroraSecondaryVariant,
    background = AuroraBackground,
    onBackground = AuroraOn,
    surface = AuroraBackground,
    onSurface = AuroraOn,
    error = Error,
    onError = Color.White
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
        // TODO - material3
        // shapes = Shapes,
        content = content
    )
}
