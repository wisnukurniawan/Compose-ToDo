package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.identifier
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.theme.CommonBlue
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalBackButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoItemCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.headerDateDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.itemInfoDisplayable
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.runtime.navigation.StepFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScheduledScreen(
    navController: NavController,
    viewModel: ScheduledViewModel
) {
    val state by viewModel.state.collectAsState()

    ScheduledContent(
        tasks = state.taskDisplayable,
        header = {
            ScheduledTitle(
                onClickBack = { navController.navigateUp() },
                text = stringResource(R.string.todo_scheduled),
                color = CommonBlue,
                backIcon = Icons.Rounded.ChevronLeft
            )
        },
        onTaskItemClick = { task, list ->
            navController.navigate(StepFlow.Root.route(task.id, list.id))
        },
        onTaskStatusItemClick = { viewModel.dispatch(ScheduledAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(ScheduledAction.TaskAction.Delete(it)) }
    )
}

@Composable
fun ScheduledTabletScreen(
    navController: NavController,
    viewModel: ScheduledViewModel
) {
    val state by viewModel.state.collectAsState()

    ScheduledContent(
        tasks = state.taskDisplayable,
        header = {
            ScheduledTitle(
                onClickBack = { navController.navigateUp() },
                text = stringResource(R.string.todo_scheduled),
                color = CommonBlue,
                backIcon = Icons.Rounded.Close
            )
        },
        onTaskItemClick = { task, list ->
            navController.navigate(StepFlow.Root.route(task.id, list.id))
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
            color = color,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ScheduledContent(
    tasks: List<ToDoTaskItem>,
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
                tasks,
                onTaskItemClick,
                onTaskStatusItemClick,
                onTaskSwipeToDelete
            )
        }
    }
}

@Composable
private fun TaskContent(
    tasks: List<ToDoTaskItem>,
    onClick: (ToDoTask, ToDoList) -> Unit,
    onStatusClick: (ToDoTask) -> Unit,
    onSwipeToDelete: (ToDoTask) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val resources = LocalContext.current.resources

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (tasks.isEmpty()) {
            item {
                PgEmpty(
                    stringResource(R.string.todo_no_task),
                    modifier = Modifier.fillParentMaxHeight()
                        .padding(bottom = 100.dp)
                )
            }
        } else {
            items(
                items = tasks,
                key = { item -> item.identifier() },
            ) {
                when (it) {
                    is ToDoTaskItem.Header -> {
                        Spacer(Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(32.dp)
                        ) {
                            Text(
                                text = it.date.headerDateDisplayable(resources),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                    is ToDoTaskItem.Complete -> {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                            PgToDoItemCell(
                                name = it.toDoTask.name,
                                color = it.toDoList.color.toColor(),
                                contentPaddingValues = PaddingValues(all = 8.dp),
                                leftIcon = Icons.Rounded.CheckCircle,
                                textDecoration = TextDecoration.LineThrough,
                                onClick = { onClick(it.toDoTask, it.toDoList) },
                                onSwipeToDelete = { onSwipeToDelete(it.toDoTask) },
                                onStatusClick = { onStatusClick(it.toDoTask) },
                                info = it.toDoTask.itemInfoDisplayable(resources, MaterialTheme.colors.error, it.toDoList.name)
                            )
                        }
                    }
                    is ToDoTaskItem.InProgress -> {
                        var isChecked by remember { mutableStateOf(false) }
                        var debounceJob: Job? by remember { mutableStateOf(null) }

                        PgToDoItemCell(
                            name = it.toDoTask.name,
                            color = it.toDoList.color.toColor(),
                            contentPaddingValues = PaddingValues(all = 8.dp),
                            leftIcon = if (isChecked) {
                                Icons.Rounded.CheckCircle
                            } else {
                                Icons.Rounded.RadioButtonUnchecked
                            },
                            textDecoration = TextDecoration.None,
                            onClick = { onClick(it.toDoTask, it.toDoList) },
                            onSwipeToDelete = { onSwipeToDelete(it.toDoTask) },
                            onStatusClick = {
                                isChecked = !isChecked
                                debounceJob?.cancel()
                                if (isChecked) {
                                    debounceJob = coroutineScope.launch {
                                        delay(1000)
                                        onStatusClick(it.toDoTask)
                                    }
                                }
                            },
                            info = it.toDoTask.itemInfoDisplayable(resources, MaterialTheme.colors.error, it.toDoList.name)
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
