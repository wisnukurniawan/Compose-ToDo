package com.wisnu.kurniawan.composetodolist.features.theme.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.theme.AuroraPrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.AuroraSecondaryVariant
import com.wisnu.kurniawan.composetodolist.foundation.theme.LightPrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.NightPrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.NightSecondaryVariant
import com.wisnu.kurniawan.composetodolist.foundation.theme.SunrisePrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.SunriseSecondaryVariant
import com.wisnu.kurniawan.composetodolist.foundation.theme.TwilightPrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.TwilightSecondaryVariant
import com.wisnu.kurniawan.composetodolist.model.Theme

@Immutable
data class ThemeState(
    val items: List<ThemeItem> = initial()
) {
    companion object {
        private fun initial() = listOf(
            ThemeItem(
                R.string.setting_theme_automatic,
                Theme.SYSTEM,
                Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        NightSecondaryVariant
                    )
                ),
                false
            ),
            ThemeItem(
                R.string.setting_theme_light,
                Theme.LIGHT,
                Brush.linearGradient(
                    colors = listOf(
                        LightPrimary,
                        Color.White
                    )
                ),
                false
            ),
            ThemeItem(
                R.string.setting_theme_twilight,
                Theme.TWILIGHT,
                Brush.linearGradient(
                    colors = listOf(
                        TwilightPrimary,
                        TwilightSecondaryVariant
                    )
                ),
                false
            ),
            ThemeItem(
                R.string.setting_theme_night,
                Theme.NIGHT,
                Brush.linearGradient(
                    colors = listOf(
                        NightPrimary,
                        NightSecondaryVariant
                    )
                ),
                false
            ),
            ThemeItem(
                R.string.setting_theme_sunrise,
                Theme.SUNRISE,
                Brush.linearGradient(
                    colors = listOf(
                        SunrisePrimary,
                        SunriseSecondaryVariant
                    )
                ),
                false
            ),
            ThemeItem(
                R.string.setting_theme_aurora,
                Theme.AURORA,
                Brush.linearGradient(
                    colors = listOf(
                        AuroraPrimary,
                        AuroraSecondaryVariant
                    )
                ),
                false
            )
        )
    }
}

data class ThemeItem(
    val title: Int,
    val theme: Theme,
    val brush: Brush,
    val applied: Boolean
)
