package com.wisnu.kurniawan.composetodolist.features.todo.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.selectedColor
import com.wisnu.kurniawan.composetodolist.foundation.theme.LargeShape
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.*
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.collectAsEffect
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.runtime.navigation.HomeFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ListDetailFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.MainFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.StepFlow

@Composable
fun ListDetailScreen(
    navController: NavController,
    viewModel: ListDetailViewModel
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsEffect()
    val listState = rememberLazyListState()

    when (effect) {
        ListDetailEffect.ShowCreateListInput -> {
            LaunchedEffect(effect) {
                navController.navigate(ListDetailFlow.CreateList.route)
            }
        }
        ListDetailEffect.ClosePage -> {
            LaunchedEffect(effect) {
                navController.navigateUp()
            }
        }
        is ListDetailEffect.ScrollTo -> {
            val position = (effect as ListDetailEffect.ScrollTo).position
            LaunchedEffect(position) {
                listState.animateScrollToItem(position)
            }
        }
        is ListDetailEffect.Relaunch -> {
            LaunchedEffect(effect) {
                val listId = (effect as ListDetailEffect.Relaunch).listId
                navController.navigate(ListDetailFlow.Root.route(listId)) {
                    popUpTo(HomeFlow.DashboardScreen.route)
                }
            }
        }
        null -> {}
    }

    ListDetailContent(
        tasks = state.listDisplayable.tasks,
        tempTaskName = state.taskName.text,
        header = {
            ListDetailTitle(
                onClick = { navController.navigate(ListDetailFlow.UpdateList.route) },
                onClickBack = { navController.navigateUp() },
                text = state.list.name,
                color = state.colors.selectedColor(),
                backIcon = Icons.Rounded.ChevronLeft
            )
        },
        color = state.colors.selectedColor(),
        onAddTaskClick = { navController.navigate(ListDetailFlow.CreateTask.route) },
        onTaskItemClick = { navController.navigate(StepFlow.Root.route(it.id, state.list.id)) },
        onTaskStatusItemClick = { viewModel.dispatch(ListDetailAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(ListDetailAction.TaskAction.Delete(it)) },
        listState = listState
    )

}

@Composable
fun ListDetailTabletScreen(
    navController: NavController,
    viewModel: ListDetailViewModel
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsEffect()
    val listState = rememberLazyListState()

    when (effect) {
        ListDetailEffect.ShowCreateListInput -> {
            LaunchedEffect(effect) {
                navController.navigate(ListDetailFlow.CreateList.route)
            }
        }
        ListDetailEffect.ClosePage -> {
            LaunchedEffect(effect) {
                navController.navigateUp()
            }
        }
        is ListDetailEffect.ScrollTo -> {
            val position = (effect as ListDetailEffect.ScrollTo).position
            LaunchedEffect(position) {
                listState.animateScrollToItem(position)
            }
        }
        is ListDetailEffect.Relaunch -> {
            LaunchedEffect(effect) {
                val listId = (effect as ListDetailEffect.Relaunch).listId
                navController.navigate(ListDetailFlow.Root.route(listId)) {
                    popUpTo(MainFlow.RootEmpty.route)
                }
            }
        }
        null -> {}
    }

    ListDetailContent(
        tasks = state.listDisplayable.tasks,
        tempTaskName = state.taskName.text,
        header = {
            ListDetailTitle(
                onClick = { navController.navigate(ListDetailFlow.UpdateList.route) },
                onClickBack = { navController.navigateUp() },
                text = state.list.name,
                color = state.colors.selectedColor(),
                backIcon = Icons.Rounded.Close
            )
        },
        color = state.colors.selectedColor(),
        onAddTaskClick = { navController.navigate(ListDetailFlow.CreateTask.route) },
        onTaskItemClick = { navController.navigate(StepFlow.Root.route(it.id, state.list.id)) },
        onTaskStatusItemClick = { viewModel.dispatch(ListDetailAction.TaskAction.OnToggleStatus(it)) },
        onTaskSwipeToDelete = { viewModel.dispatch(ListDetailAction.TaskAction.Delete(it)) },
        listState = listState
    )

}

@Composable
private fun ListDetailTitle(
    onClick: () -> Unit,
    onClickBack: () -> Unit,
    text: String,
    color: Color,
    backIcon: ImageVector
) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
            maxLines = 1,
            color = color,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ListDetailContent(
    tasks: List<ToDoTaskItem>,
    tempTaskName: String,
    header: @Composable ColumnScope.() -> Unit,
    onAddTaskClick: () -> Unit,
    onTaskItemClick: (ToDoTask) -> Unit,
    onTaskStatusItemClick: (ToDoTask) -> Unit,
    onTaskSwipeToDelete: (ToDoTask) -> Unit,
    color: Color,
    listState: LazyListState
) {
    PgPageLayout {
        header()

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TaskContent(
                Modifier.weight(1F),
                tasks,
                onTaskItemClick,
                onTaskStatusItemClick,
                onTaskSwipeToDelete,
                color,
                listState
            )

            TaskCreator(
                color = color,
                onClick = onAddTaskClick,
                text = tempTaskName
            )
        }

    }
}

@Composable
fun CreateListEditor(
    viewModel: ListDetailViewModel,
    navController: NavController
) {
    val shouldClosePage = remember { mutableStateOf(true) }
    val state by viewModel.state.collectAsState()
    val defaultName = stringResource(R.string.todo_create_list_default_name)

    DisposableEffect(Unit) {
        onDispose {
            if (shouldClosePage.value) {
                navController.navigateUp()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dispatch(ListDetailAction.ListAction.InitName(defaultName))
    }

    CreateListEditor(
        title = stringResource(R.string.todo_create_list),
        positiveText = stringResource(R.string.todo_create_list_positive),
        name = state.newListName,
        isValidName = state.validListName,
        colorItems = state.colors,
        onNameChange = { viewModel.dispatch(ListDetailAction.ListAction.ChangeName(it)) },
        onCancelClick = { navController.navigateUp() },
        onSaveClick = {
            shouldClosePage.value = false

            navController.navigateUp()

            viewModel.dispatch(ListDetailAction.ListAction.Create)
        },
        onSelectColor = { viewModel.dispatch(ListDetailAction.ListAction.ApplyColor(it)) }
    )
}

@Composable
fun UpdateListEditor(
    viewModel: ListDetailViewModel,
    navController: NavController
) {
    val shouldRollback = remember { mutableStateOf(true) }
    val state by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            if (shouldRollback.value) {
                viewModel.dispatch(ListDetailAction.ListAction.CancelUpdate)
            }
        }
    }

    CreateListEditor(
        title = stringResource(R.string.todo_rename_list),
        positiveText = stringResource(R.string.todo_rename_list_positive),
        name = state.newListName,
        isValidName = state.validListName,
        colorItems = state.colors,
        onNameChange = { viewModel.dispatch(ListDetailAction.ListAction.ChangeName(it)) },
        onCancelClick = { navController.navigateUp() },
        onSaveClick = {
            shouldRollback.value = false

            navController.navigateUp()

            viewModel.dispatch(ListDetailAction.ListAction.Update)
        },
        onSelectColor = { viewModel.dispatch(ListDetailAction.ListAction.ApplyColor(it)) }
    )
}

@Composable
fun CreateListEditor(
    title: String,
    positiveText: String,
    name: String,
    isValidName: Boolean,
    colorItems: List<ColorItem>,
    onNameChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    onSelectColor: (ColorItem) -> Unit,
) {
    PgModalLayout(
        title = {
            PgModalTitle(
                text = title,
                textColor = colorItems.selectedColor()
            )
        },
        content = {
            item {
                PgTextField(
                    value = name,
                    onValueChange = { onNameChange(it) },
                    placeholderValue = stringResource(R.string.todo_create_list_hint),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp).height(50.dp).fillMaxWidth(),
                    shape = LargeShape,
                    textColor = colorItems.selectedColor()
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(colorItems) {
                        PgIconButton(
                            onClick = { onSelectColor(it) },
                            modifier = Modifier.size(54.dp),
                            color = Color.Transparent,
                        ) {
                            Box(
                                Modifier
                                    .background(
                                        color = it.color,
                                        shape = CircleShape
                                    )
                                    .size(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (it.applied) {
                                    Box(
                                        Modifier
                                            .background(color = Color.White, shape = CircleShape)
                                            .size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.padding(16.dp)) {
                    PgSecondaryButton(
                        modifier = Modifier.weight(1F),
                        onClick = onCancelClick,
                    ) { Text(text = stringResource(R.string.todo_cancel), color = MaterialTheme.colorScheme.onSecondary) }

                    Spacer(Modifier.width(16.dp))

                    PgButton(
                        modifier = Modifier.weight(1F),
                        onClick = onSaveClick,
                        enabled = isValidName
                    ) { Text(text = positiveText, color = Color.White) }
                }
            }
        }
    )
}

