package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
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
import com.wisnu.kurniawan.composetodolist.foundation.theme.*
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeToDismiss

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
    onScheduledTodayTask: () -> Unit,
    onScheduledTask: () -> Unit,
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
                    .padding(all = 16.dp)
            ) {
                ScheduledTodayCell(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    onClick = onScheduledTodayTask,
                    currentDate = currentDate,
                    scheduledTaskCount = scheduledTodayTaskCount
                )

                ScheduledCell(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    onClick = onScheduledTask,
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
                    modifier = Modifier.fillParentMaxHeight()
                        .padding(bottom = 100.dp)
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
            Spacer(Modifier.height(66.dp))
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
                        .size(24.dp)
                        .background(shape = CircleShape, color = CommonRed),
                    contentAlignment = Alignment.Center
                ) {
                    PgIcon(
                        imageVector = Icons.Rounded.CalendarToday,
                        modifier = Modifier
                            .size(16.dp)
                    )

                    Text(
                        currentDate,
                        fontSize = 8.sp,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Text(
                    text = scheduledTaskCount,
                    style = MaterialTheme.typography.h6,
                )
            }

            Spacer(Modifier.height(8.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = stringResource(R.string.todo_today),
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ScheduledCell(
    modifier: Modifier,
    onClick: () -> Unit,
    scheduledTaskCount: String
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
                        .size(24.dp)
                        .background(shape = CircleShape, color = CommonBlue),
                    contentAlignment = Alignment.Center
                ) {
                    PgIcon(
                        imageVector = Icons.Rounded.Event,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }

                Text(
                    text = scheduledTaskCount,
                    style = MaterialTheme.typography.h6,
                )
            }

            Spacer(Modifier.height(8.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = stringResource(R.string.todo_scheduled),
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AllTaskCell(
    modifier: Modifier,
    onClick: () -> Unit,
    allTaskCount: String
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
                        .size(24.dp)
                        .background(shape = CircleShape, color = CommonGrey),
                    contentAlignment = Alignment.Center
                ) {
                    PgIcon(
                        imageVector = Icons.Rounded.Inbox,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }

                Text(
                    text = allTaskCount,
                    style = MaterialTheme.typography.h6,
                )
            }

            Spacer(Modifier.height(8.dp))

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = stringResource(R.string.todo_all),
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
    SwipeToDismiss(
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
                                .size(24.dp)
                                .background(shape = CircleShape, color = color),
                            contentAlignment = Alignment.Center
                        ) {
                            PgIcon(
                                imageVector = Icons.Rounded.List,
                                modifier = Modifier
                                    .size(16.dp)
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
