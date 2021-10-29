package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.composetodolist.foundation.theme.MediumShape

@Composable
fun PgModalCell(
    onClick: () -> Unit,
    text: String,
    color: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    leftIcon: @Composable (() -> Unit)? = null,
    rightIcon: @Composable (() -> Unit)? = null
) {
    val colorAlpha = if (enabled) {
        1F
    } else {
        0.3F
    }
    val onClickState = if (enabled) {
        onClick
    } else {
        {}
    }
    val indication = if (enabled) {
        LocalIndication.current
    } else {
        null
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        shape = MediumShape,
        color = color.copy(alpha = colorAlpha),
        onClick = onClickState,
        indication = indication
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leftIcon != null) {
                Spacer(Modifier.width(8.dp))
                leftIcon()
                Spacer(Modifier.width(16.dp))
            } else {
                Spacer(Modifier.width(20.dp))
            }

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )

            if (rightIcon != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    rightIcon()
                    Spacer(Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun PgToDoItemCell(
    name: String,
    info: AnnotatedString? = null,
    color: Color,
    contentPaddingValues: PaddingValues,
    leftIcon: ImageVector,
    textDecoration: TextDecoration?,
    onClick: () -> Unit,
    onSwipeToDelete: () -> Unit,
    onStatusClick: () -> Unit
) {
    SwipeDismiss(
        backgroundModifier = Modifier
            .background(MaterialTheme.colorScheme.secondary),
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onClick
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(contentPaddingValues)
                    ) {
                        PgIconButton(
                            onClick = onStatusClick,
                            color = Color.Transparent
                        ) {
                            PgIcon(
                                imageVector = leftIcon,
                                tint = color
                            )
                        }

                        Column {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyLarge.copy(textDecoration = textDecoration),
                            )

                            if (info != null) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = info,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7F)
                                )
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(start = 56.dp))
                }
            }
        },
        onDismiss = { onSwipeToDelete() }
    )
}
