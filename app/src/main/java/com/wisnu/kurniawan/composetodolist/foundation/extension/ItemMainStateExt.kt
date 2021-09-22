package com.wisnu.kurniawan.composetodolist.foundation.extension

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.foundation.theme.MediumRadius

fun ItemMainState.ItemListType.cellShape() = when (this) {
    is ItemMainState.ItemListType.First -> {
        RoundedCornerShape(
            topStart = MediumRadius,
            topEnd = MediumRadius
        )
    }
    is ItemMainState.ItemListType.Last -> {
        RoundedCornerShape(
            bottomStart = MediumRadius,
            bottomEnd = MediumRadius
        )
    }
    is ItemMainState.ItemListType.Middle -> {
        RectangleShape
    }
    is ItemMainState.ItemListType.Single -> {
        RoundedCornerShape(size = MediumRadius)
    }
}

fun ItemMainState.identifier() = when (this) {
    is ItemMainState.ItemGroup -> group.id
    is ItemMainState.ItemListType -> list.id
}
