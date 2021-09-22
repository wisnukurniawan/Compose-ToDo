package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PgModalBackButton(
    onClick: () -> Unit,
) {
    PgIconButton(
        onClick = onClick,
        modifier = Modifier.size(28.dp)
    ) {
        PgIcon(
            imageVector = Icons.Rounded.ChevronLeft,
        )
    }
}

@Composable
fun PgIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colors.secondary,
    content: @Composable () -> Unit
) {
    val shape = CircleShape
    IconButton(
        onClick = onClick,
        modifier = modifier.background(
            color = color,
            shape = shape
        ).clip(shape),
        enabled = enabled
    ) {
        content()
    }
}

@Composable
fun PgButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier.height(56.dp),
        enabled = enabled,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        content = content,
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
        ),
    )
}

@Composable
fun PgSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier = modifier.height(56.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        content = content
    )
}
