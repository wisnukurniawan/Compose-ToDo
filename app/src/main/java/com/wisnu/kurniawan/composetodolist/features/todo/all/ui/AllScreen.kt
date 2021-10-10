package com.wisnu.kurniawan.composetodolist.features.todo.all.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.identifier
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalBackButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoItemCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.itemInfoDisplayable
import com.wisnu.kurniawan.composetodolist.model.ToDoList
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
                backIcon = Icons.Rounded.ChevronLeft
            )
        },
        onTaskItemClick = { task, list ->
            navController.navigate(StepFlow.Root.route(task.id, list.id))
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
                backIcon = Icons.Rounded.Close
            )
        },
        onTaskItemClick = { task, list ->
            navController.navigate(StepFlow.Root.route(task.id, list.id))
        },
        onTaskStatusItemClick = { viewModel.dispatch(AllAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(AllAction.TaskAction.Delete(it)) }
    )
}

@Composable
private fun AllTitle(
    onClickBack: () -> Unit,
    text: String,
    backIcon: ImageVector
) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart)
        ) {
            PgModalBackButton(onClickBack, backIcon)
        }

        Text(
            text = text,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun AllContent(
    items: List<ItemAllState>,
    header: @Composable ColumnScope.() -> Unit,
    onTaskItemClick: (ToDoTask, ToDoList) -> Unit,
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

@Composable
private fun TaskContent(
    items: List<ItemAllState>,
    onClick: (ToDoTask, ToDoList) -> Unit,
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
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                    is ItemAllState.Complete -> {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                            PgToDoItemCell(
                                name = it.task.name,
                                color = it.list.color.toColor(),
                                contentPaddingValues = PaddingValues(all = 8.dp),
                                leftIcon = Icons.Rounded.CheckCircle,
                                textDecoration = TextDecoration.LineThrough,
                                onClick = { onClick(it.task, it.list) },
                                onSwipeToDelete = { onSwipeToDelete(it.task) },
                                onStatusClick = { onStatusClick(it.task) },
                                info = it.task.itemInfoDisplayable(resources, MaterialTheme.colors.error)
                            )
                        }
                    }
                    is ItemAllState.InProgress -> {
                        var isChecked by remember { mutableStateOf(false) }
                        var debounceJob: Job? by remember { mutableStateOf(null) }

                        PgToDoItemCell(
                            name = it.task.name,
                            color = it.list.color.toColor(),
                            contentPaddingValues = PaddingValues(all = 8.dp),
                            leftIcon = if (isChecked) {
                                Icons.Rounded.CheckCircle
                            } else {
                                Icons.Rounded.RadioButtonUnchecked
                            },
                            textDecoration = TextDecoration.None,
                            onClick = { onClick(it.task, it.list) },
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
                            info = it.task.itemInfoDisplayable(resources, MaterialTheme.colors.error)
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
