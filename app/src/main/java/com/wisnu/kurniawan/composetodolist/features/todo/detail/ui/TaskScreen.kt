package com.wisnu.kurniawan.composetodolist.features.todo.detail.ui

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaDisabled
import com.wisnu.kurniawan.composetodolist.foundation.theme.SmallShape
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
            shape = SmallShape,
            color = MaterialTheme.colorScheme.secondary,
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = color
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskContent(
    modifier: Modifier,
    tasks: List<ToDoTaskItem>,
    onClick: (ToDoTask) -> Unit,
    onStatusClick: (ToDoTask) -> Unit,
    onSwipeToDelete: (ToDoTask) -> Unit,
    color: Color,
    listState: LazyListState
) {
    val coroutineScope = rememberCoroutineScope()
    val resources = LocalContext.current.resources

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
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
                                style = MaterialTheme.typography.bodyLarge,
                                color = color
                            )
                        }
                    }
                    is ToDoTaskItem.Complete -> {
                        PgToDoItemCell(
                            modifier = Modifier.animateItemPlacement(),
                            name = it.toDoTask.name,
                            color = color.copy(alpha = AlphaDisabled),
                            contentPaddingValues = PaddingValues(all = 8.dp),
                            leftIcon = Icons.Rounded.CheckCircle,
                            textDecoration = TextDecoration.LineThrough,
                            onClick = { onClick(it.toDoTask) },
                            onSwipeToDelete = { onSwipeToDelete(it.toDoTask) },
                            onStatusClick = { onStatusClick(it.toDoTask) },
                            info = it.toDoTask.itemInfoDisplayable(resources, MaterialTheme.colorScheme.error)
                        )
                    }
                    is ToDoTaskItem.InProgress -> {
                        var isChecked by remember { mutableStateOf(false) }
                        var debounceJob: Job? by remember { mutableStateOf(null) }

                        PgToDoItemCell(
                            modifier = Modifier.animateItemPlacement(),
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
                            info = it.toDoTask.itemInfoDisplayable(resources, MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(250.dp))
        }
    }
}

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
