package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.theme.ui.ThemeItem
import com.wisnu.kurniawan.composetodolist.model.Theme

fun List<ThemeItem>.update(theme: Theme): List<ThemeItem> {
    return map {
        it.copy(applied = it.theme == theme)
    }
}
