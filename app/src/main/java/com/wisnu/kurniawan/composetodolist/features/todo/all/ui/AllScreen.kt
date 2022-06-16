package com.wisnu.kurniawan.composetodolist.features.todo.all.ui

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
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.identifier
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaDisabled
import com.wisnu.kurniawan.composetodolist.foundation.theme.Shapes
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoItemCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.itemInfoDisplayable
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.runtime.navigation.StepFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AllScreen(
    navController: NavController,
    viewModel: AllViewModel
) {
    val state by viewModel.state.collectAsState()

    AllContent(
        items = state.items,
        header = {
            AllTitle(
                onClickBack = { navController.navigateUp() },
                text = stringResource(R.string.todo_all),
                backIcon = Icons.Rounded.ChevronLeft,
                hideCompleteTask = state.hideCompleteTask,
                onShowHideCompleteTaskClick = {
                    viewModel.dispatch(AllAction.ToggleCompleteTaskVisibility)
                }
            )
        },
        onTaskItemClick = {
            navController.navigate(StepFlow.Root.route(it.task.id, it.list.id))
        },
        onTaskStatusItemClick = { viewModel.dispatch(AllAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(AllAction.TaskAction.Delete(it)) }
    )
}

@Composable
fun AllTabletScreen(
    navController: NavController,
    viewModel: AllViewModel
) {
    val state by viewModel.state.collectAsState()

    AllContent(
        items = state.items,
        header = {
            AllTitle(
                onClickBack = { navController.navigateUp() },
                text = stringResource(R.string.todo_all),
                backIcon = Icons.Rounded.Close,
                hideCompleteTask = state.hideCompleteTask,
                onShowHideCompleteTaskClick = {
                    viewModel.dispatch(AllAction.ToggleCompleteTaskVisibility)
                }
            )
        },
        onTaskItemClick = {
            navController.navigate(StepFlow.Root.route(it.task.id, it.list.id))
        },
        onTaskStatusItemClick = { viewModel.dispatch(AllAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(AllAction.TaskAction.Delete(it)) }
    )
}

@Composable
private fun AllTitle(
    onClickBack: () -> Unit,
    text: String,
    backIcon: ImageVector,
    hideCompleteTask: Boolean,
    onShowHideCompleteTaskClick: () -> Unit
) {
    var moreMenuExpanded by remember { mutableStateOf(false) }

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
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .padding(end = 4.dp)
                .align(Alignment.CenterEnd)
        ) {
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

@Composable
private fun AllContent(
    items: List<ItemAllState>,
    header: @Composable ColumnScope.() -> Unit,
    onTaskItemClick: (ItemAllState.Task) -> Unit,
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
                items = items,
                onClick = onTaskItemClick,
                onStatusClick = onTaskStatusItemClick,
                onSwipeToDelete = onTaskSwipeToDelete,
                emptyPage = {
                    PgEmpty(
                        stringResource(R.string.todo_no_task),
                        modifier = Modifier.fillMaxSize()
                            .padding(bottom = 100.dp)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskContent(
    items: List<ItemAllState>,
    onClick: (ItemAllState.Task) -> Unit,
    onStatusClick: (ToDoTask) -> Unit,
    onSwipeToDelete: (ToDoTask) -> Unit,
    emptyPage: @Composable () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val resources = LocalContext.current.resources

    if (items.isEmpty()) {
        emptyPage()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            this.items(
                items = items,
                key = { item -> item.identifier() },
            ) {
                when (it) {
                    is ItemAllState.List -> {
                        Spacer(Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(32.dp)
                        ) {
                            Text(
                                text = it.list.name,
                                maxLines = 1,
                                color = it.list.color.toColor(),
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    is ItemAllState.Task.Complete -> {
                        PgToDoItemCell(
                            modifier = Modifier.animateItemPlacement(),
                            name = it.task.name,
                            color = it.list.color.toColor().copy(alpha = AlphaDisabled),
                            contentPaddingValues = PaddingValues(all = 8.dp),
                            leftIcon = Icons.Rounded.CheckCircle,
                            textDecoration = TextDecoration.LineThrough,
                            onClick = { onClick(it) },
                            onSwipeToDelete = { onSwipeToDelete(it.task) },
                            onStatusClick = { onStatusClick(it.task) },
                            info = it.task.itemInfoDisplayable(resources, MaterialTheme.colorScheme.error)
                        )
                    }
                    is ItemAllState.Task.InProgress -> {
                        var isChecked by remember { mutableStateOf(false) }
                        var debounceJob: Job? by remember { mutableStateOf(null) }

                        PgToDoItemCell(
                            modifier = Modifier.animateItemPlacement(),
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
                            info = it.task.itemInfoDisplayable(resources, MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
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
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
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
