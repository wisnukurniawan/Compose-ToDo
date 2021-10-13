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
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.wisnu.kurniawan.composetodolist.foundation.theme.CommonGrey
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBlue
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.theme.MediumRadius
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeDismiss

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoMainScreen(
    data: List<ItemMainState>,
    currentDate: String,
    scheduledTodayTaskCount: String,
    scheduledTaskCount: String,
    allTaskCount: String,
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
                    scheduledTaskCount = scheduledTodayTaskCount
                )

                ScheduledCell(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    onClick = onClickScheduledTask,
                    scheduledTaskCount = scheduledTaskCount
                )
            }
        }

        item {
            AllTaskCell(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                onClick = onClickAllTask,
                allTaskCount = allTaskCount
            )
        }

        item {
            Text(
                text = stringResource(R.string.todo_my_list),
                style = MaterialTheme.typography.h6,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduledTodayCell(
    modifier: Modifier,
    onClick: () -> Unit,
    currentDate: String,
    scheduledTaskCount: String
) {
    OverallTaskCell(
        modifier = modifier,
        taskCount = scheduledTaskCount,
        title = stringResource(R.string.todo_today),
        iconText = currentDate,
        icon = Icons.Rounded.CalendarToday,
        iconColor = ListRed,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduledCell(
    modifier: Modifier,
    onClick: () -> Unit,
    scheduledTaskCount: String
) {
    OverallTaskCell(
        modifier = modifier,
        taskCount = scheduledTaskCount,
        title = stringResource(R.string.todo_scheduled),
        icon = Icons.Rounded.Event,
        iconColor = ListBlue,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AllTaskCell(
    modifier: Modifier,
    onClick: () -> Unit,
    allTaskCount: String
) {
    OverallTaskCell(
        modifier = modifier,
        taskCount = allTaskCount,
        title = stringResource(R.string.todo_all),
        icon = Icons.Rounded.Inbox,
        iconColor = CommonGrey,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun OverallTaskCell(
    modifier: Modifier,
    taskCount: String,
    title: String,
    icon: ImageVector,
    iconColor: Color,
    iconText: String = "",
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(size = MediumRadius),
        onClick = onClick,
        color = MaterialTheme.colors.secondaryVariant
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
                        .background(shape = CircleShape, color = iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    PgIcon(
                        imageVector = icon,
                        modifier = Modifier
                            .size(20.dp)
                    )

                    if (iconText.isNotBlank()) {
                        Text(
                            text = iconText,
                            fontSize = 8.sp,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Text(
                    text = taskCount,
                    style = MaterialTheme.typography.h5,
                )
            }

            Spacer(Modifier.height(8.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
                style = MaterialTheme.typography.subtitle2,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ListCell(
    shape: Shape,
    name: String,
    totalTask: String,
    color: Color,
    shouldShowDivider: Boolean,
    onClick: () -> Unit,
    onSwipeToDelete: () -> Unit,
) {
    SwipeDismiss(
        backgroundModifier = Modifier
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colors.secondary, shape),
        backgroundSecondaryModifier = Modifier.clip(shape),
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = shape,
                onClick = onClick,
                color = MaterialTheme.colors.secondaryVariant
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
                            style = MaterialTheme.typography.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.size(8.dp))

                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                            Text(
                                text = totalTask,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }

                    if (shouldShowDivider) {
                        PgDivider()
                    }
                }
            }
        },
        onDismiss = { onSwipeToDelete() }
    )
}

@Composable
private fun PgDivider() {
    Row {
        Spacer(
            Modifier
                .width(48.dp)
                .height(1.dp)
                .background(color = MaterialTheme.colors.secondaryVariant)
        )
        Divider()
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
        {},
        {}
    )
}
