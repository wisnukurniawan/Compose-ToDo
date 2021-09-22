package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun PgTransparentFooter(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    MaterialTheme.colors.surface,
                    MaterialTheme.colors.surface,
                )
            )
        ).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        content = content
    )
}
