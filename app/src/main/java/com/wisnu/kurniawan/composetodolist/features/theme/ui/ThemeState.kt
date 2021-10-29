package com.wisnu.kurniawan.composetodolist.features.theme.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import com.wisnu.kurniawan.composetodolist.model.Theme

@Immutable
data class ThemeState(
    val items: List<ThemeItem> = listOf()
)

data class ThemeItem(
    val title: Int,
    val theme: Theme,
    val brush: Brush,
    val applied: Boolean
)
