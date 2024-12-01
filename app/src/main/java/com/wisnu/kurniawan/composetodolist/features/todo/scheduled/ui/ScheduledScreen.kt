package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.identifier
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaDisabled
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBlue
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.theme.Shapes
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoItemCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.headerDateDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.itemInfoDisplayable
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScheduledScreen(
    viewModel: ScheduledViewModel,
    backIcon: ImageVector,
    onClickBack: () -> Unit,
    onTaskItemClick: (String, String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScheduledContent(
        items = state.items,
        header = {
            ScheduledTitle(
                onClickBack = onClickBack,
                text = stringResource(R.string.todo_scheduled),
                color = ListBlue,
                backIcon = backIcon,
                canToggleCompletedTask = true,
                hideCompleteTask = state.hideCompleteTask,
                onShowHideCompleteTaskClick = {
                    viewModel.dispatch(ScheduledAction.ToggleCompleteTaskVisibility)
                },
            )
        },
        onTaskItemClick = {
            onTaskItemClick(it.task.id, it.list.id)
        },
        onTaskStatusItemClick = { viewModel.dispatch(ScheduledAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(ScheduledAction.TaskAction.Delete(it)) }
    )
}

@Composable
fun ScheduledTodayScreen(
    viewModel: ScheduledViewModel,
    backIcon: ImageVector,
    onClickBack: () -> Unit,
    onTaskItemClick: (String, String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScheduledContent(
        items = state.items,
        header = {
            ScheduledTitle(
                onClickBack = onClickBack,
                text = stringResource(R.string.todo_today),
                color = ListRed,
                backIcon = backIcon
            )
        },
        onTaskItemClick = {
            onTaskItemClick(it.task.id, it.list.id)
        },
        onTaskStatusItemClick = { viewModel.dispatch(ScheduledAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(ScheduledAction.TaskAction.Delete(it)) }
    )
}

@Composable
private fun ScheduledTitle(
    onClickBack: () -> Unit,
    text: String,
    color: Color,
    backIcon: ImageVector,
    canToggleCompletedTask: Boolean = false,
    hideCompleteTask: Boolean = false,
    onShowHideCompleteTaskClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterStart)
        ) {
            PgIconButton(
                onClick = onClickBack,
                color = Color.Transparent
            ) {
                PgIcon(imageVector = backIcon)
            }
        }

        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )

        if (canToggleCompletedTask) {
            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .align(Alignment.CenterEnd)
            ) {
                var moreMenuExpanded by remember { mutableStateOf(false) }

                PgIconButton(
                    onClick = { moreMenuExpanded = true },
                    color = Color.Transparent
                ) {
                    PgIcon(
                        imageVector = Icons.Rounded.MoreVert,
                    )
                }

                MoreMenu(
                    expanded = moreMenuExpanded,
                    hideCompleteTask = hideCompleteTask,
                    onDismissRequest = { moreMenuExpanded = false },
                    onShowHideCompleteTaskClick = {
                        onShowHideCompleteTaskClick()
                        moreMenuExpanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ScheduledContent(
    items: List<ItemScheduledState>,
    header: @Composable ColumnScope.() -> Unit,
    onTaskItemClick: (ItemScheduledState.Task) -> Unit,
    onTaskStatusItemClick: (ToDoTask) -> Unit,
    onTaskSwipeToDelete: (ToDoTask) -> Unit
) {
    PgPageLayout {
        header()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
        ) {
            TaskContent(
                items,
                onTaskItemClick,
                onTaskStatusItemClick,
                onTaskSwipeToDelete
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskContent(
    items: List<ItemScheduledState>,
    onClick: (ItemScheduledState.Task) -> Unit,
    onStatusClick: (ToDoTask) -> Unit,
    onSwipeToDelete: (ToDoTask) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val resources = LocalContext.current.resources

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (items.isEmpty()) {
            item {
                PgEmpty(
                    stringResource(R.string.todo_no_task),
                    modifier = Modifier.fillParentMaxHeight()
                        .padding(bottom = 100.dp)
                )
            }
        } else {
            this.items(
                items = items,
                key = { item -> item.identifier() },
            ) {
                when (it) {
                    is ItemScheduledState.Header -> {
                        Spacer(Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(32.dp)
                        ) {
                            Text(
                                text = it.date.headerDateDisplayable(resources),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                    is ItemScheduledState.Task.Complete -> {
                        PgToDoItemCell(
                            modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                            name = it.task.name,
                            color = it.list.color.toColor().copy(alpha = AlphaDisabled),
                            contentPaddingValues = PaddingValues(all = 8.dp),
                            leftIcon = Icons.Rounded.CheckCircle,
                            textDecoration = TextDecoration.LineThrough,
                            onClick = { onClick(it) },
                            onSwipeToDelete = { onSwipeToDelete(it.task) },
                            onStatusClick = { onStatusClick(it.task) },
                            info = it.task.itemInfoDisplayable(resources, MaterialTheme.colorScheme.error, it.list.name)
                        )
                    }
                    is ItemScheduledState.Task.InProgress -> {
                        var isChecked by remember { mutableStateOf(false) }
                        var debounceJob: Job? by remember { mutableStateOf(null) }

                        PgToDoItemCell(
                            modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                            name = it.task.name,
                            color = it.list.color.toColor(),
                            contentPaddingValues = PaddingValues(all = 8.dp),
                            leftIcon = if (isChecked) {
                                Icons.Rounded.CheckCircle
                            } else {
                                Icons.Rounded.RadioButtonUnchecked
                            },
                            textDecoration = TextDecoration.None,
                            onClick = { onClick(it) },
                            onSwipeToDelete = { onSwipeToDelete(it.task) },
                            onStatusClick = {
                                isChecked = !isChecked
                                debounceJob?.cancel()
                                if (isChecked) {
                                    debounceJob = coroutineScope.launch {
                                        delay(1000)
                                        onStatusClick(it.task)
                                    }
                                }
                            },
                            info = it.task.itemInfoDisplayable(resources, MaterialTheme.colorScheme.error, it.list.name)
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MoreMenu(
    expanded: Boolean,
    hideCompleteTask: Boolean,
    onDismissRequest: () -> Unit,
    onShowHideCompleteTaskClick: () -> Unit
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(medium = Shapes.small)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(220.dp).background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            DropdownMenuItem(
                text = {
                    val label = if (hideCompleteTask) {
                        stringResource(id = R.string.todo_show_complete)
                    } else {
                        stringResource(id = R.string.todo_hide_complete)
                    }

                    Text(
                        label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                onClick = onShowHideCompleteTaskClick,
                leadingIcon = {
                    val icon = if (hideCompleteTask) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    PgIcon(imageVector = icon)
                }
            )
        }
    }
}
