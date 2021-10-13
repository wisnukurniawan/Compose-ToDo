package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Schedule
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.displayable
import com.wisnu.kurniawan.composetodolist.foundation.extension.isDueDateSet
import com.wisnu.kurniawan.composetodolist.foundation.extension.isExpired
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBlue
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.theme.MediumRadius
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoItemCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.dateTimeDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.dueDateDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.timeDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.collectAsEffect
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.showDatePicker
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.showTimePicker
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.runtime.navigation.StepFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun StepScreen(
    navController: NavController,
    viewModel: StepViewModel
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsEffect()
    val activity = LocalContext.current as AppCompatActivity
    val listState = rememberLazyListState()

    when (effect) {
        is StepEffect.ScrollTo -> {
            val position = (effect as StepEffect.ScrollTo).position
            LaunchedEffect(position) {
                listState.animateScrollToItem(position)
            }
        }
    }

    StepScreen(
        onClickBack = { navController.navigateUp() },
        task = state.task,
        steps = state.task.steps,
        color = state.color.toColor(),
        listState = listState,
        onClickTaskName = { navController.navigate(StepFlow.EditTask.route) },
        onClickTaskStatus = { viewModel.dispatch(StepAction.TaskAction.OnToggleStatus) },
        onClickCreateStep = { navController.navigate(StepFlow.CreateStep.route) },
        onClickStep = { navController.navigate(StepFlow.EditStep.route(it.id)) },
        onClickStepStatus = { viewModel.dispatch(StepAction.StepItemAction.Edit.OnToggleStatus(it)) },
        onSwipeToDeleteStep = { viewModel.dispatch(StepAction.StepItemAction.Edit.Delete(it)) },
        onClickTaskDelete = {
            viewModel.dispatch(StepAction.TaskAction.Delete)
            navController.navigateUp()
        },
        onClickDueDateItem = {
            val initial = state.task.dueDate?.toLocalDate()
            if (initial != null) {
                activity.showDatePicker(initial) { selectedDate ->
                    viewModel.dispatch(StepAction.TaskAction.SelectDueDate(selectedDate))
                }
            }
        },
        onClickTimeItem = {
            val initial = state.task.dueDate?.toLocalTime()
            if (initial != null) {
                activity.showTimePicker(initial) { selectedTime ->
                    viewModel.dispatch(StepAction.TaskAction.SelectTime(selectedTime))
                }
            }
        },
        onClickRepeatItem = { navController.navigate(StepFlow.SelectRepeatTask.route) },
        onCheckedChangeDueDateItem = { checked ->
            if (checked) {
                activity.showDatePicker(DateTimeProviderImpl().now().toLocalDate()) { selectedDate ->
                    viewModel.dispatch(StepAction.TaskAction.SelectDueDate(selectedDate))
                }
            } else {
                viewModel.dispatch(StepAction.TaskAction.ResetDueDate)
            }
        },
        onCheckedChangeTimeItem = { checked ->
            if (checked) {
                val nextHour = DateTimeProviderImpl().now().toLocalTime().plusHours(1)
                val initialValue = LocalTime.of(nextHour.hour, 0)
                activity.showTimePicker(initialValue) { selectedTime ->
                    viewModel.dispatch(StepAction.TaskAction.SelectTime(selectedTime))
                }
            } else {
                viewModel.dispatch(StepAction.TaskAction.ResetTime)
            }
        }
    )
}

@Composable
private fun StepScreen(
    onClickBack: () -> Unit,
    task: ToDoTask,
    steps: List<ToDoStep>,
    color: Color,
    listState: LazyListState,
    onClickTaskName: () -> Unit,
    onClickTaskStatus: () -> Unit,
    onClickCreateStep: () -> Unit,
    onClickStep: (ToDoStep) -> Unit,
    onClickStepStatus: (ToDoStep) -> Unit,
    onSwipeToDeleteStep: (ToDoStep) -> Unit,
    onClickTaskDelete: () -> Unit,
    onClickDueDateItem: () -> Unit,
    onClickTimeItem: () -> Unit,
    onClickRepeatItem: () -> Unit,
    onCheckedChangeDueDateItem: (Boolean) -> Unit,
    onCheckedChangeTimeItem: (Boolean) -> Unit,
) {
    val resources = LocalContext.current.resources

    PgPageLayout {
        StepTitle(onClickBack = onClickBack)

        TaskCell(
            task = task,
            color = color,
            onClickTaskName = onClickTaskName,
            onClickTaskStatus = onClickTaskStatus
        )

        StepContent(
            modifier = Modifier.weight(1f),
            task = task,
            dueDateTitle = task.dueDateDisplayable(resources) ?: stringResource(R.string.todo_add_due_date_task),
            dueDateTimeTitle = task.timeDisplayable() ?: stringResource(R.string.todo_add_due_date_time_task),
            steps = steps,
            color = color,
            listState = listState,
            onClickCreateStep = onClickCreateStep,
            onClickStep = onClickStep,
            onClickStepStatus = onClickStepStatus,
            onSwipeToDeleteStep = onSwipeToDeleteStep,
            onClickDueDateItem = onClickDueDateItem,
            onClickTimeItem = onClickTimeItem,
            onClickRepeatItem = onClickRepeatItem,
            onCheckedChangeDueDateItem = onCheckedChangeDueDateItem,
            onCheckedChangeTimeItem = onCheckedChangeTimeItem
        )

        StepFooter(
            title = task.dateTimeDisplayable(),
            onClickDelete = onClickTaskDelete
        )
    }
}

@Composable
private fun StepTitle(
    onClickBack: () -> Unit
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
                PgIcon(imageVector = Icons.Rounded.ChevronLeft)
            }
        }
    }
}

@Composable
private fun TaskCell(
    task: ToDoTask,
    color: Color,
    onClickTaskName: () -> Unit,
    onClickTaskStatus: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    when (task.status) {
        ToDoStatus.IN_PROGRESS -> {
            var isChecked by remember { mutableStateOf(false) }
            var debounceJob: Job? by remember { mutableStateOf(null) }

            TaskCell(
                name = task.name,
                color = color,
                leftIcon = if (isChecked) {
                    Icons.Rounded.CheckCircle
                } else {
                    Icons.Rounded.RadioButtonUnchecked
                },
                textDecoration = TextDecoration.None,
                onClick = onClickTaskName,
                onClickStatus = {
                    isChecked = !isChecked
                    debounceJob?.cancel()
                    if (isChecked) {
                        debounceJob = coroutineScope.launch {
                            delay(1000)
                            onClickTaskStatus()
                        }
                    }
                }
            )
        }
        ToDoStatus.COMPLETE -> {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                TaskCell(
                    name = task.name,
                    color = color,
                    leftIcon = Icons.Rounded.CheckCircle,
                    textDecoration = TextDecoration.LineThrough,
                    onClick = onClickTaskName,
                    onClickStatus = onClickTaskStatus
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TaskCell(
    name: String,
    color: Color,
    leftIcon: ImageVector,
    textDecoration: TextDecoration,
    onClick: () -> Unit,
    onClickStatus: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = 8.dp)
            ) {
                PgIconButton(
                    onClick = onClickStatus,
                    color = Color.Transparent,
                    modifier = Modifier.size(56.dp)
                ) {
                    PgIcon(
                        imageVector = leftIcon,
                        tint = color,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = name,
                    style = MaterialTheme.typography.h6.copy(textDecoration = textDecoration),
                )
            }

            Divider(modifier = Modifier.padding(start = 56.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StepContent(
    modifier: Modifier,
    task: ToDoTask,
    dueDateTitle: String,
    dueDateTimeTitle: String,
    steps: List<ToDoStep>,
    color: Color,
    listState: LazyListState,
    onClickStep: (ToDoStep) -> Unit,
    onClickStepStatus: (ToDoStep) -> Unit,
    onSwipeToDeleteStep: (ToDoStep) -> Unit,
    onClickCreateStep: () -> Unit,
    onClickDueDateItem: () -> Unit,
    onClickTimeItem: () -> Unit,
    onClickRepeatItem: () -> Unit,
    onCheckedChangeDueDateItem: (Boolean) -> Unit,
    onCheckedChangeTimeItem: (Boolean) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        items(items = steps, key = { item -> item.id }) { item ->
            StepCell(
                item = item,
                color = color,
                onClick = { onClickStep(item) },
                onClickStatus = { onClickStepStatus(item) },
                onSwipeToDelete = { onSwipeToDeleteStep(item) }
            )
        }

        item {
            StepCellCreator(
                color = color,
                onClick = onClickCreateStep
            )
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            ActionCell(
                title = dueDateTitle,
                shape = RoundedCornerShape(
                    topStart = MediumRadius,
                    topEnd = MediumRadius
                ),
                iconBgColor = ListRed,
                leftIcon = Icons.Rounded.Event,
                showDivider = true,
                onClick = if (task.isDueDateSet()) {
                    onClickDueDateItem
                } else {
                    null
                },
                trailing = {
                    Switch(
                        checked = task.isDueDateSet(),
                        onCheckedChange = onCheckedChangeDueDateItem,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colors.primary,
                            uncheckedThumbColor = MaterialTheme.colors.onSurface
                        )
                    )
                },
                titleColor = if (task.isExpired()) {
                    MaterialTheme.colors.error
                } else {
                    Color.Unspecified
                }
            )
            ActionCell(
                title = dueDateTimeTitle,
                shape = RoundedCornerShape(
                    bottomStart = MediumRadius,
                    bottomEnd = MediumRadius
                ),
                iconBgColor = ListBlue,
                leftIcon = Icons.Rounded.Schedule,
                showDivider = false,
                onClick = if (task.isDueDateTimeSet) {
                    onClickTimeItem
                } else {
                    null
                },
                trailing = {
                    Switch(
                        checked = task.isDueDateTimeSet,
                        onCheckedChange = onCheckedChangeTimeItem,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colors.primary,
                            uncheckedThumbColor = MaterialTheme.colors.onSurface
                        )
                    )
                },
                titleColor = if (task.isExpired() && task.isDueDateTimeSet) {
                    MaterialTheme.colors.error
                } else {
                    Color.Unspecified
                }
            )
            Spacer(Modifier.height(16.dp))

            if (task.isDueDateSet()) {
                ActionCell(
                    title = stringResource(R.string.todo_add_due_date_repeat_task),
                    shape = RoundedCornerShape(size = MediumRadius),
                    iconBgColor = Color(0xFF656567),
                    leftIcon = Icons.Rounded.Repeat,
                    showDivider = false,
                    onClick = onClickRepeatItem,
                    trailing = {
                        Row {
                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                                Text(
                                    text = stringResource(task.repeat.displayable()),
                                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal)
                                )
                                Spacer(Modifier.width(8.dp))
                                PgIcon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                )
                            }
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
            }
        }

        item {
            Spacer(Modifier.height(70.dp))
        }
    }
}

@Composable
private fun StepCell(
    item: ToDoStep,
    color: Color,
    onClick: () -> Unit,
    onClickStatus: () -> Unit,
    onSwipeToDelete: () -> Unit,
) {
    when (item.status) {
        ToDoStatus.IN_PROGRESS -> {
            PgToDoItemCell(
                name = item.name,
                color = color,
                contentPaddingValues = PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                leftIcon = Icons.Rounded.RadioButtonUnchecked,
                textDecoration = TextDecoration.None,
                onClick = onClick,
                onSwipeToDelete = onSwipeToDelete,
                onStatusClick = onClickStatus
            )
        }
        ToDoStatus.COMPLETE -> {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                PgToDoItemCell(
                    name = item.name,
                    color = color,
                    contentPaddingValues = PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    leftIcon = Icons.Rounded.CheckCircle,
                    textDecoration = TextDecoration.LineThrough,
                    onClick = onClick,
                    onSwipeToDelete = onSwipeToDelete,
                    onStatusClick = onClickStatus
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StepCellCreator(
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
        ) {
            PgIconButton(
                onClick = onClick,
                color = Color.Transparent,
            ) {
                PgIcon(
                    imageVector = Icons.Rounded.Add,
                    tint = color
                )
            }

            Text(
                text = stringResource(R.string.todo_step_next),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ActionCell(
    title: String,
    titleColor: Color = Color.Unspecified,
    shape: Shape,
    iconBgColor: Color,
    leftIcon: ImageVector,
    showDivider: Boolean,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit
) {
    if (onClick != null) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = shape,
            onClick = onClick,
            color = MaterialTheme.colors.secondaryVariant
        ) {
            ActionContentCell(
                title = title,
                titleColor = titleColor,
                iconBgColor = iconBgColor,
                leftIcon = leftIcon,
                showDivider = showDivider,
                trailing = trailing
            )
        }
    } else {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = shape,
            color = MaterialTheme.colors.secondaryVariant
        ) {
            ActionContentCell(
                title = title,
                titleColor = titleColor,
                iconBgColor = iconBgColor,
                leftIcon = leftIcon,
                showDivider = showDivider,
                trailing = trailing
            )
        }
    }
}

@Composable
private fun ActionContentCell(
    title: String,
    titleColor: Color,
    iconBgColor: Color,
    leftIcon: ImageVector,
    showDivider: Boolean,
    trailing: @Composable () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(shape = RoundedCornerShape(size = 6.dp), color = iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                PgIcon(
                    imageVector = leftIcon,
                    modifier = Modifier
                        .size(20.dp)
                )
            }

            Spacer(Modifier.size(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f),
                color = titleColor
            )
            Spacer(Modifier.size(8.dp))

            trailing()
        }

        if (showDivider) {
            Row {
                Spacer(
                    Modifier
                        .width(52.dp)
                        .height(1.dp)
                        .background(color = MaterialTheme.colors.secondaryVariant)
                )
                Divider()
            }
        }
    }
}

@Composable
private fun StepFooter(
    title: String,
    onClickDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(42.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            PgIconButton(
                onClick = onClickDelete,
                color = Color.Transparent,
                modifier = Modifier
                    .size(42.dp)
            ) {
                PgIcon(
                    imageVector = Icons.Rounded.Delete,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.padding(end = 12.dp))
        }
    }
}
