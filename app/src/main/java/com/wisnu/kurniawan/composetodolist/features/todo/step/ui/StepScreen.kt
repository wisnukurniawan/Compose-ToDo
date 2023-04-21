package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wisnu.foundation.coredatetime.toLocalDateTime
import com.wisnu.foundation.coredatetime.toMillis
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.displayable
import com.wisnu.kurniawan.composetodolist.foundation.extension.isExpired
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaDisabled
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaMedium
import com.wisnu.kurniawan.composetodolist.foundation.theme.CommonGrey
import com.wisnu.kurniawan.composetodolist.foundation.theme.DividerAlpha
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBlue
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.theme.MediumRadius
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTimePickerDialog
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoItemCell
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.dateTimeDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.dueDateDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.noteUpdatedAtDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.timeDisplayable
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.HandleEffect
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun StepScreen(
    viewModel: StepViewModel,
    onClickBack: () -> Unit,
    onClickTaskName: () -> Unit,
    onClickCreateStep: () -> Unit,
    onClickStep: (String) -> Unit,
    onClickTaskDelete: () -> Unit,
    onClickRepeatItem: () -> Unit,
    onClickUpdateNote: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    HandleEffect(viewModel) {
        when (it) {
            is StepEffect.ScrollTo -> {
                listState.animateScrollToItem(it.position)
            }
        }
    }

    StepScreen(
        onClickBack = onClickBack,
        task = state.task,
        steps = state.task.steps,
        color = state.color.toColor(),
        listState = listState,

        showDueDatePicker = state.showDueDatePicker,
        dueDateInitial = state.dueDateInitial,

        showDueTimePicker = state.showDueTimePicker,
        dueTimeInitial = state.dueTimeInitial,

        onClickTaskName = onClickTaskName,
        onClickTaskStatus = { viewModel.dispatch(StepAction.TaskAction.OnToggleStatus) },
        onClickCreateStep = onClickCreateStep,
        onClickStep = { onClickStep(it.id) },
        onClickStepStatus = { viewModel.dispatch(StepAction.StepItemAction.Edit.OnToggleStatus(it)) },
        onSwipeToDeleteStep = { viewModel.dispatch(StepAction.StepItemAction.Edit.Delete(it)) },
        onClickTaskDelete = {
            viewModel.dispatch(StepAction.TaskAction.Delete)
            onClickTaskDelete()
        },

        onClickDueDateItem = {
            viewModel.dispatch(StepAction.TaskAction.EditDueDate)
        },
        onClickDueDateItemSelect = {
            viewModel.dispatch(StepAction.TaskAction.SelectDueDate(it))
        },
        onClickDueDateItemCancel = {
            viewModel.dispatch(StepAction.TaskAction.DismissDueDatePicker)
        },
        onCheckDueDateItemChange = { checked ->
            viewModel.dispatch(StepAction.TaskAction.ChangeDueDate(checked))
        },

        onClickDueTimeItem = {
            viewModel.dispatch(StepAction.TaskAction.EditDueTime)
        },
        onClickDueTimeItemSelect = {
            viewModel.dispatch(StepAction.TaskAction.SelectDueTime(it))
        },
        onClickDueTimeItemCancel = {
            viewModel.dispatch(StepAction.TaskAction.DismissDueTimePicker)
        },
        onCheckDueTimeItemChange = { checked ->
            viewModel.dispatch(StepAction.TaskAction.ChangeDueTime(checked))
        },

        onClickRepeatItem = onClickRepeatItem,
        onClickUpdateNote = onClickUpdateNote
    )
}

@Composable
private fun StepScreen(
    onClickBack: () -> Unit,
    task: ToDoTask,
    steps: List<ToDoStep>,
    color: Color,
    listState: LazyListState,

    showDueDatePicker: Boolean,
    dueDateInitial: LocalDate,

    showDueTimePicker: Boolean,
    dueTimeInitial: LocalTime,

    onClickTaskName: () -> Unit,
    onClickTaskStatus: () -> Unit,
    onClickCreateStep: () -> Unit,
    onClickStep: (ToDoStep) -> Unit,
    onClickStepStatus: (ToDoStep) -> Unit,
    onSwipeToDeleteStep: (ToDoStep) -> Unit,
    onClickTaskDelete: () -> Unit,

    onClickDueDateItem: () -> Unit,
    onClickDueDateItemSelect: (LocalDate?) -> Unit,
    onClickDueDateItemCancel: () -> Unit,
    onCheckDueDateItemChange: (Boolean) -> Unit,

    onClickDueTimeItem: () -> Unit,
    onClickDueTimeItemSelect: (LocalTime) -> Unit,
    onClickDueTimeItemCancel: () -> Unit,
    onCheckDueTimeItemChange: (Boolean) -> Unit,

    onClickRepeatItem: () -> Unit,
    onClickUpdateNote: () -> Unit,
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
            note = task.note,
            noteUpdatedAtTitle = task.noteUpdatedAtDisplayable(),
            listState = listState,

            showDueDatePicker = showDueDatePicker,
            dueDateInitial = dueDateInitial,

            showDueTimePicker = showDueTimePicker,
            dueTimeInitial = dueTimeInitial,

            onClickCreateStep = onClickCreateStep,
            onClickStep = onClickStep,
            onClickStepStatus = onClickStepStatus,
            onSwipeToDeleteStep = onSwipeToDeleteStep,

            onClickDueDateItem = onClickDueDateItem,
            onClickDueDateItemSelect = onClickDueDateItemSelect,
            onClickDueDateItemCancel = onClickDueDateItemCancel,
            onCheckDueDateItemChange = onCheckDueDateItemChange,

            onClickDueTimeItem = onClickDueTimeItem,
            onClickDueTimeItemSelect = onClickDueTimeItemSelect,
            onClickDueTimeItemCancel = onClickDueTimeItemCancel,
            onCheckDueTimeItemChange = onCheckDueTimeItemChange,

            onClickRepeatItem = onClickRepeatItem,
            onClickUpdateNote = onClickUpdateNote
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
            TaskCell(
                name = task.name,
                color = color.copy(alpha = AlphaDisabled),
                leftIcon = Icons.Rounded.CheckCircle,
                textDecoration = TextDecoration.LineThrough,
                onClick = onClickTaskName,
                onClickStatus = onClickTaskStatus
            )
        }
    }
}

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
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    style = MaterialTheme.typography.titleMedium.copy(textDecoration = textDecoration),
                )
            }

            Divider(
                modifier = Modifier.padding(start = 56.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = DividerAlpha)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun StepContent(
    modifier: Modifier,
    task: ToDoTask,
    dueDateTitle: String,
    dueDateTimeTitle: String,
    steps: List<ToDoStep>,
    note: String,
    noteUpdatedAtTitle: String,
    color: Color,
    listState: LazyListState,

    showDueDatePicker: Boolean,
    dueDateInitial: LocalDate,

    showDueTimePicker: Boolean,
    dueTimeInitial: LocalTime,

    onClickStep: (ToDoStep) -> Unit,
    onClickStepStatus: (ToDoStep) -> Unit,
    onSwipeToDeleteStep: (ToDoStep) -> Unit,
    onClickCreateStep: () -> Unit,

    onClickDueDateItem: () -> Unit,
    onClickDueDateItemSelect: (LocalDate?) -> Unit,
    onClickDueDateItemCancel: () -> Unit,
    onCheckDueDateItemChange: (Boolean) -> Unit,

    onClickDueTimeItem: () -> Unit,
    onClickDueTimeItemSelect: (LocalTime) -> Unit,
    onClickDueTimeItemCancel: () -> Unit,
    onCheckDueTimeItemChange: (Boolean) -> Unit,

    onClickRepeatItem: () -> Unit,
    onClickUpdateNote: () -> Unit,
) {
    if (showDueTimePicker) {
        val timePickerState = remember {
            TimePickerState(
                initialHour = dueTimeInitial.hour,
                initialMinute = dueTimeInitial.minute,
                is24Hour = false
            )
        }
        PgTimePickerDialog(
            onCancel = { onClickDueTimeItemCancel() },
            onConfirm = {
                onClickDueTimeItemSelect(LocalTime.of(timePickerState.hour, timePickerState.minute))
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    if (showDueDatePicker) {
        val datePickerState = remember {
            DatePickerState(
                initialSelectedDateMillis = dueDateInitial.toMillis(ZoneId.ofOffset("UTC", ZoneOffset.UTC)),
                initialDisplayedMonthMillis = dueDateInitial.toMillis(ZoneId.ofOffset("UTC", ZoneOffset.UTC)),
                yearRange = DatePickerDefaults.YearRange,
                initialDisplayMode = DisplayMode.Picker
            )
        }
        val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(
            onDismissRequest = onClickDueDateItemCancel,
            confirmButton = {
                TextButton(
                    onClick = {
                        onClickDueDateItemSelect(datePickerState.selectedDateMillis?.toLocalDateTime()?.toLocalDate())
                    },
                    enabled = confirmEnabled
                ) { Text("Oke") }
            },
            dismissButton = {
                TextButton(
                    onClick = onClickDueDateItemCancel
                ) { Text(stringResource(R.string.todo_cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        items(items = steps, key = { item -> item.id }) { item ->
            StepCell(
                modifier = Modifier.animateItemPlacement(),
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
                        onCheckedChange = onCheckDueDateItemChange,
                    )
                },
                titleColor = if (task.isExpired()) {
                    MaterialTheme.colorScheme.error
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
                    onClickDueTimeItem
                } else {
                    null
                },
                trailing = {
                    Switch(
                        checked = task.isDueDateTimeSet,
                        onCheckedChange = onCheckDueTimeItemChange,
                    )
                },
                titleColor = if (task.isExpired() && task.isDueDateTimeSet) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Unspecified
                }
            )
            Spacer(Modifier.height(16.dp))

            if (task.isDueDateSet()) {
                ActionCell(
                    title = stringResource(R.string.todo_add_due_date_repeat_task),
                    shape = RoundedCornerShape(size = MediumRadius),
                    iconBgColor = CommonGrey,
                    leftIcon = Icons.Rounded.Repeat,
                    showDivider = false,
                    onClick = onClickRepeatItem,
                    trailing = {
                        Row {
                            Text(
                                text = stringResource(task.repeat.displayable()),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = AlphaDisabled)
                            )
                            Spacer(Modifier.width(8.dp))
                            PgIcon(
                                imageVector = Icons.Rounded.ChevronRight,
                                tint = LocalContentColor.current.copy(alpha = AlphaDisabled)
                            )
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
            }
        }

        item {
            val shape = RoundedCornerShape(size = MediumRadius)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(shape)
                    .clickable(onClick = onClickUpdateNote),
                shape = shape,
                color = MaterialTheme.colorScheme.secondary
            ) {
                Column(
                    modifier = Modifier.padding(all = 16.dp)
                ) {
                    if (note.isBlank()) {
                        Text(
                            text = stringResource(R.string.todo_add_note),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    } else {
                        Text(
                            text = note,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 6,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.size(8.dp))

                        Text(
                            text = stringResource(R.string.todo_note) + "・" + noteUpdatedAtTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = AlphaMedium)
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(70.dp))
        }
    }
}

@Composable
private fun StepCell(
    modifier: Modifier,
    item: ToDoStep,
    color: Color,
    onClick: () -> Unit,
    onClickStatus: () -> Unit,
    onSwipeToDelete: () -> Unit,
) {
    when (item.status) {
        ToDoStatus.IN_PROGRESS -> {
            PgToDoItemCell(
                modifier = modifier,
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
            PgToDoItemCell(
                modifier = modifier,
                name = item.name,
                color = color.copy(alpha = AlphaDisabled),
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

@Composable
private fun StepCellCreator(
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

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
                .padding(horizontal = 16.dp)
                .clip(shape)
                .clickable(onClick = onClick),
            shape = shape,
            color = MaterialTheme.colorScheme.secondary
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
            color = MaterialTheme.colorScheme.secondary
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
                style = MaterialTheme.typography.titleSmall,
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
                        .background(color = MaterialTheme.colorScheme.secondary)
                )
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = DividerAlpha))
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
            style = MaterialTheme.typography.labelMedium,
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
