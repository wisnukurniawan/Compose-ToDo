package com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.DriveFileRenameOutline
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalTitle

@Composable
fun GroupMenuScreen(
    viewModel: GroupMenuViewModel,
    onAddRemoveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onRenameClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GroupMenuScreen(
        items = state.items,
        onAddRemoveClick = onAddRemoveClick,
        onDeleteClick = {
            onDeleteClick()
            viewModel.dispatch(GroupMenuAction.ClickDelete)
        },
        onRenameClick = onRenameClick
    )
}

@Composable
private fun GroupMenuScreen(
    items: List<GroupMenuItem>,
    onAddRemoveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onRenameClick: () -> Unit,
) {
    PgModalLayout(
        title = {
            PgModalTitle(stringResource(R.string.todo_group_menu))
        },
        content = {
            items(items) { item ->
                when (item) {
                    is GroupMenuItem.AddRemove -> {
                        GroupMenuItem(
                            onClick = onAddRemoveClick,
                            title = stringResource(item.title),
                            imageVector = Icons.AutoMirrored.Rounded.List,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            enabled = item.enabled
                        )
                    }
                    is GroupMenuItem.Delete -> {
                        GroupMenuItem(
                            onClick = onDeleteClick,
                            title = stringResource(item.title),
                            imageVector = item.icon,
                            color = MaterialTheme.colorScheme.primary,
                            enabled = item.enabled
                        )
                    }
                    is GroupMenuItem.Rename -> {
                        GroupMenuItem(
                            onClick = onRenameClick,
                            title = stringResource(item.title),
                            imageVector = Icons.Rounded.DriveFileRenameOutline,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            enabled = item.enabled
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    )
}

@Composable
private fun GroupMenuItem(
    onClick: () -> Unit,
    title: String,
    imageVector: ImageVector,
    color: Color,
    enabled: Boolean
) {
    PgModalCell(
        onClick = onClick,
        text = title,
        color = color,
        leftIcon = @Composable {
            PgIcon(
                imageVector = imageVector
            )
        },
        enabled = enabled
    )
}

