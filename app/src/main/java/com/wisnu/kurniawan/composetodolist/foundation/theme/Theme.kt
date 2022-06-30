package com.wisnu.kurniawan.composetodolist.foundation.theme

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.model.Theme

val LightColorPalette = lightColorScheme(
    primary = LightPrimary,
    primaryContainer = LightPrimary,
    secondary = LightItemBackgroundL1,
    secondaryContainer = LightItemBackgroundL1,
    background = LightBackgroundL2,
    surface = LightBackgroundL1,
    surfaceVariant = LightItemBackgroundL2,
    error = LightError,
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    onSecondary = LightOn,
    onSecondaryContainer = LightOn,
    onBackground = LightOn,
    onSurface = LightOn,
    onSurfaceVariant = LightOn,
    onError = Color.White
)

val TwilightColorPalette = lightColorScheme(
    primary = TwilightPrimary,
    primaryContainer = TwilightPrimary,
    secondary = TwilightItemBackgroundL1,
    secondaryContainer = TwilightItemBackgroundL1,
    background = TwilightBackgroundL2,
    surface = TwilightBackgroundL1,
    surfaceVariant = TwilightItemBackgroundL2,
    error = Error,
    onPrimary = TwilightOn,
    onPrimaryContainer = TwilightOn,
    onSecondary = TwilightOn,
    onSecondaryContainer = TwilightOn,
    onBackground = TwilightOn,
    onSurface = TwilightOn,
    onSurfaceVariant = TwilightOn,
    onError = Color.White
)

val NightColorPalette = darkColorScheme(
    primary = NightPrimary,
    primaryContainer = NightPrimary,
    secondary = NightItemBackgroundL1,
    secondaryContainer = NightItemBackgroundL1,
    background = NightBackgroundL2,
    surface = NightBackgroundL1,
    surfaceVariant = NightItemBackgroundL2,
    error = Error,
    onPrimary = NightOn,
    onPrimaryContainer = NightOn,
    onSecondary = NightOn,
    onSecondaryContainer = NightOn,
    onBackground = NightOn,
    onSurface = NightOn,
    onSurfaceVariant = NightOn,
    onError = Color.White
)

val SunriseColorPalette = darkColorScheme(
    primary = SunrisePrimary,
    primaryContainer = SunrisePrimary,
    secondary = SunriseItemBackgroundL1,
    secondaryContainer = SunriseItemBackgroundL1,
    background = SunriseBackgroundL2,
    surface = SunriseBackgroundL1,
    surfaceVariant = SunriseItemBackgroundL2,
    error = SunriseError,
    onPrimary = SunriseOn,
    onPrimaryContainer = SunriseOn,
    onSecondary = SunriseOn,
    onSecondaryContainer = SunriseOn,
    onBackground = SunriseOn,
    onSurface = SunriseOn,
    onSurfaceVariant = SunriseOn,
    onError = Color.White
)

val AuroraColorPalette = darkColorScheme(
    primary = AuroraPrimary,
    primaryContainer = AuroraPrimary,
    secondary = AuroraItemBackgroundL1,
    secondaryContainer = AuroraItemBackgroundL1,
    background = AuroraBackgroundL2,
    surface = AuroraBackgroundL1,
    surfaceVariant = AuroraItemBackgroundL2,
    error = Error,
    onPrimary = AuroraOn,
    onPrimaryContainer = AuroraOn,
    onSecondary = AuroraOn,
    onSecondaryContainer = AuroraOn,
    onBackground = AuroraOn,
    onSurface = AuroraOn,
    onSurfaceVariant = AuroraOn,
    onError = Color.White
)

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
    val activity = LocalContext.current as AppCompatActivity

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = darkIcons,
            isNavigationBarContrastEnforced = false
        )
    }

    LaunchedEffect(colors) {
        when (colors) {
            LightColorPalette -> activity.setTheme(R.style.Theme_ComposeToDoList_Light)
            TwilightColorPalette -> activity.setTheme(R.style.Theme_ComposeToDoList_Twilight)
            NightColorPalette -> activity.setTheme(R.style.Theme_ComposeToDoList_Night)
            SunriseColorPalette -> activity.setTheme(R.style.Theme_ComposeToDoList_Sunrise)
            AuroraColorPalette -> activity.setTheme(R.style.Theme_ComposeToDoList_Aurora)
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
