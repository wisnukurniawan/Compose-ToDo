package com.wisnu.kurniawan.composetodolist.foundation.extension

import androidx.compose.ui.graphics.Color
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ColorItem

fun List<ColorItem>.update(color: Color): List<ColorItem> {
    return map {
        it.copy(applied = it.color == color)
    }
}

fun List<ColorItem>.selectedColor(): Color {
    return find { it.applied }?.color ?: Color.White
}
