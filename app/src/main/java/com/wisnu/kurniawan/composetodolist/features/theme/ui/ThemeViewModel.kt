package com.wisnu.kurniawan.composetodolist.features.theme.ui

import android.os.Build
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.features.theme.data.IThemeEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.extension.select
import com.wisnu.kurniawan.composetodolist.foundation.theme.AuroraPrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.AuroraSecondaryVariant
import com.wisnu.kurniawan.composetodolist.foundation.theme.LightPrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.NightPrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.NightSecondaryVariant
import com.wisnu.kurniawan.composetodolist.foundation.theme.SunrisePrimary
import com.wisnu.kurniawan.composetodolist.foundation.theme.SunriseSecondaryVariant
import com.wisnu.kurniawan.composetodolist.foundation.theme.TwilightItemBackgroundL1
import com.wisnu.kurniawan.composetodolist.foundation.theme.TwilightPrimary
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    themeEnvironment: IThemeEnvironment,
) :
    StatefulViewModel<ThemeState, Unit, ThemeAction, IThemeEnvironment>(ThemeState(), themeEnvironment) {

    init {
        initTheme()
    }

    override fun dispatch(action: ThemeAction) {
        when (action) {
            is ThemeAction.SelectTheme -> applyTheme(action.selected)
        }
    }

    private fun initTheme() {
        viewModelScope.launch {
            setState { copy(items = initial()) }

            environment.getTheme()
                .collect {
                    setState { copy(items = items.select(it)) }
                }
        }
    }

    private fun applyTheme(item: ThemeItem) {
        viewModelScope.launch {
            environment.setTheme(item.theme)
        }
    }

    private fun initial(): List<ThemeItem> {
        val data = mutableListOf<ThemeItem>()

        data.add(
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
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            data.add(
                ThemeItem(
                    R.string.setting_theme_wallpaper,
                    Theme.WALLPAPER,
                    Brush.linearGradient(
                        colors = listOf()
                    ),
                    false
                )
            )
        }

        data.add(
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
            )
        )

        data.add(
            ThemeItem(
                R.string.setting_theme_twilight,
                Theme.TWILIGHT,
                Brush.linearGradient(
                    colors = listOf(
                        TwilightPrimary,
                        TwilightItemBackgroundL1
                    )
                ),
                false
            )
        )

        data.add(
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
            )
        )

        data.add(
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
            )
        )

        data.add(
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

        return data
    }

}
