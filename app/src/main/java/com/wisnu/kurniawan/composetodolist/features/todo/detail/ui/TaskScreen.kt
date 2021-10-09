package com.wisnu.kurniawan.composetodolist.features.todo.detail.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.identifier
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoCreator
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoItemCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.itemInfoDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.requestFocusImeAware
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskCreator(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                .height(56.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colors.secondary,
            onClick = onClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(12.dp))

                PgIcon(
                    imageVector = Icons.Rounded.Add,
                    tint = color
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = if (text.isBlank()) {
                        stringResource(R.string.todo_add_task)
                    } else {
                        text
                    },
                    style = MaterialTheme.typography.body1,
                    color = color
                )
            }
        }
    }
}

@Composable
fun TaskContent(
    tasks: List<ToDoTaskItem>,
    onClick: (ToDoTask) -> Unit,
    onStatusClick: (ToDoTask) -> Unit,
    onSwipeToDelete: (ToDoTask) -> Unit,
    color: Color
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
                    is ToDoTaskItem.CompleteHeader -> {
                        Spacer(Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(32.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.todo_add_task_completed),
                                style = MaterialTheme.typography.body1,
                                color = color
                            )
                        }
                    }
                    is ToDoTaskItem.Complete -> {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                            PgToDoItemCell(
                                name = it.toDoTask.name,
                                color = color,
                                contentPaddingValues = PaddingValues(all = 8.dp),
                                leftIcon = Icons.Rounded.CheckCircle,
                                textDecoration = TextDecoration.LineThrough,
                                onClick = { onClick(it.toDoTask) },
                                onSwipeToDelete = { onSwipeToDelete(it.toDoTask) },
                                onStatusClick = { onStatusClick(it.toDoTask) },
                                info = it.toDoTask.itemInfoDisplayable(resources, MaterialTheme.colors.error)
                            )
                        }
                    }
                    is ToDoTaskItem.InProgress -> {
                        var isChecked by remember { mutableStateOf(false) }
                        var debounceJob: Job? by remember { mutableStateOf(null) }

                        PgToDoItemCell(
                            name = it.toDoTask.name,
                            color = color,
                            contentPaddingValues = PaddingValues(all = 8.dp),
                            leftIcon = if (isChecked) {
                                Icons.Rounded.CheckCircle
                            } else {
                                Icons.Rounded.RadioButtonUnchecked
                            },
                            textDecoration = TextDecoration.None,
                            onClick = { onClick(it.toDoTask) },
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
                            info = it.toDoTask.itemInfoDisplayable(resources, MaterialTheme.colors.error)
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskEditor(
    viewModel: ListDetailViewModel,
) {
    val focusRequest = remember { FocusRequester() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        launch { focusRequest.requestFocusImeAware() }
        viewModel.dispatch(ListDetailAction.TaskAction.OnShow)
    }

    PgToDoCreator(
        value = state.taskName,
        modifier = Modifier.focusRequester(focusRequest),
        isValid = state.validTaskName,
        placeholder = stringResource(R.string.todo_add_task),
        onValueChange = { viewModel.dispatch(ListDetailAction.TaskAction.ChangeTaskName(it)) },
        onSubmit = { viewModel.dispatch(ListDetailAction.TaskAction.ClickSubmit) },
        onNextSubmit = { viewModel.dispatch(ListDetailAction.TaskAction.ClickImeDone) }
    )
}
