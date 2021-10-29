package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.cellShape
import com.wisnu.kurniawan.composetodolist.foundation.extension.identifier
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.extension.totalTask
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaDisabled
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaHight
import com.wisnu.kurniawan.composetodolist.foundation.theme.CommonGrey
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBlue
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.theme.MediumRadius
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeDismiss

@Composable
fun ToDoMainScreen(
    data: List<ItemMainState>,
    currentDate: String,
    scheduledTodayTaskCount: String,
    scheduledTaskCount: String,
    allTaskCount: String,
    isAllTaskSelected: Boolean,
    isScheduledTodayTaskSelected: Boolean,
    isScheduledTaskSelected: Boolean,
    onClickGroup: (ItemMainState.ItemGroup) -> Unit,
    onClickList: (ItemMainState.ItemListType) -> Unit,
    onSwipeToDelete: (ItemMainState.ItemListType) -> Unit,
    onClickScheduledTodayTask: () -> Unit,
    onClickScheduledTask: () -> Unit,
    onClickAllTask: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
            ) {
                ScheduledTodayCell(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    onClick = onClickScheduledTodayTask,
                    currentDate = currentDate,
                    scheduledTaskCount = scheduledTodayTaskCount,
                    isSelected = isScheduledTodayTaskSelected
                )

                ScheduledCell(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    onClick = onClickScheduledTask,
                    scheduledTaskCount = scheduledTaskCount,
                    isSelected = isScheduledTaskSelected
                )
            }
        }

        item {
            AllTaskCell(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                onClick = onClickAllTask,
                allTaskCount = allTaskCount,
                isSelected = isAllTaskSelected
            )
        }

        item {
            Text(
                text = stringResource(R.string.todo_my_list),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
            )
        }

        if (data.isEmpty()) {
            item {
                PgEmpty(
                    stringResource(R.string.todo_no_list),
                    modifier = Modifier
                        .height(300.dp)
                )
            }
        } else {
            items(
                items = data,
                key = { item -> item.identifier() }
            ) { item ->
                when (item) {
                    is ItemMainState.ItemGroup -> {
                        GroupCell({ onClickGroup(item) }, item.group.name)
                    }
                    is ItemMainState.ItemListType -> {
                        ListCell(
                            shape = item.cellShape(),
                            name = item.list.name,
                            totalTask = item.list.totalTask().toString(),
                            color = item.list.color.toColor(),
                            shouldShowDivider = item is ItemMainState.ItemListType.First || item is ItemMainState.ItemListType.Middle,
                            isSelected = item.selected,
                            onClick = { onClickList(item) },
                            onSwipeToDelete = { onSwipeToDelete(item) }
                        )

                        if (item is ItemMainState.ItemListType.Single ||
                            item is ItemMainState.ItemListType.Last
                        ) {
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
private fun ScheduledTodayCell(
    modifier: Modifier,
    onClick: () -> Unit,
    currentDate: String,
    scheduledTaskCount: String,
    isSelected: Boolean
) {
    OverallTaskCell(
        modifier = modifier,
        taskCount = scheduledTaskCount,
        title = stringResource(R.string.todo_today),
        iconText = currentDate,
        icon = Icons.Rounded.CalendarToday,
        iconColor = ListRed,
        onClick = onClick,
        isSelected = isSelected
    )
}

@Composable
private fun ScheduledCell(
    modifier: Modifier,
    onClick: () -> Unit,
    scheduledTaskCount: String,
    isSelected: Boolean
) {
    OverallTaskCell(
        modifier = modifier,
        taskCount = scheduledTaskCount,
        title = stringResource(R.string.todo_scheduled),
        icon = Icons.Rounded.Event,
        iconColor = ListBlue,
        onClick = onClick,
        isSelected = isSelected
    )
}

@Composable
private fun AllTaskCell(
    modifier: Modifier,
    onClick: () -> Unit,
    allTaskCount: String,
    isSelected: Boolean
) {
    OverallTaskCell(
        modifier = modifier,
        taskCount = allTaskCount,
        title = stringResource(R.string.todo_all),
        icon = Icons.Rounded.Inbox,
        iconColor = CommonGrey,
        onClick = onClick,
        isSelected = isSelected
    )
}

@Composable
private fun OverallTaskCell(
    modifier: Modifier,
    taskCount: String,
    title: String,
    icon: ImageVector,
    iconColor: Color,
    iconText: String = "",
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(size = MediumRadius),
        onClick = onClick,
        color = if (isSelected) {
            iconColor
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        }
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            shape = CircleShape,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onSecondary
                            } else {
                                iconColor
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    PgIcon(
                        imageVector = icon,
                        modifier = Modifier
                            .size(20.dp),
                        tint = if (isSelected) {
                            iconColor
                        } else {
                            MaterialTheme.colorScheme.onSecondary
                        }
                    )

                    if (iconText.isNotBlank()) {
                        Text(
                            text = iconText,
                            fontSize = 8.sp,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 2.dp),
                            color = if (isSelected) {
                                iconColor
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }

                Text(
                    text = taskCount,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(8.dp))

            val alpha = if (isSelected) {
                AlphaHight
            } else {
                AlphaDisabled
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha)
            )
        }
    }
}

@Composable
private fun GroupCell(
    onClickGroup: () -> Unit,
    name: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(horizontal = 16.dp),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            PgIconButton(
                onClick = onClickGroup,
                color = Color.Transparent,
            ) {
                PgIcon(
                    imageVector = Icons.Rounded.MoreHoriz,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ListCell(
    shape: Shape,
    name: String,
    totalTask: String,
    color: Color,
    shouldShowDivider: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSwipeToDelete: () -> Unit,
) {
    SwipeDismiss(
        backgroundModifier = Modifier
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.secondary, shape),
        backgroundSecondaryModifier = Modifier.clip(shape),
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = shape,
                onClick = onClick,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
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
                                .background(shape = CircleShape, color = color),
                            contentAlignment = Alignment.Center
                        ) {
                            PgIcon(
                                imageVector = Icons.Rounded.List,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.size(8.dp))

                        Text(
                            text = totalTask,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = AlphaDisabled)
                        )
                    }

                    if (shouldShowDivider) {
                        PgDivider(
                            needSpacer = !isSelected,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            }
                        )
                    }
                }
            }
        },
        onDismiss = { onSwipeToDelete() }
    )
}

@Composable
private fun PgDivider(
    needSpacer: Boolean,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
) {
    Row {
        if (needSpacer) {
            Spacer(
                Modifier
                    .width(48.dp)
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            )
        }
        Divider(color = color)
    }
}

@Preview
@Composable
fun CellPreview() {
    ListCell(
        RectangleShape,
        "Name",
        "23",
        ListRed,
        true,
        false,
        {},
        {}
    )
}
