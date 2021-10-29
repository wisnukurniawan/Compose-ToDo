package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PgIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = "",
        tint = tint,
        modifier = modifier
    )
}
