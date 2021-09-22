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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.runtime.Composable
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
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.cellShape
import com.wisnu.kurniawan.composetodolist.foundation.extension.identifier
import com.wisnu.kurniawan.composetodolist.foundation.extension.toColor
import com.wisnu.kurniawan.composetodolist.foundation.extension.totalTask
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.AnimatedSwipeDismiss
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgEmpty
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoMainScreen(
    data: List<ItemMainState>,
    onClickGroup: (ItemMainState.ItemGroup) -> Unit,
    onClickList: (ItemMainState.ItemListType) -> Unit,
    onSwipeToDelete: (ItemMainState.ItemListType) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(
                text = stringResource(R.string.todo_my_list),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))
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
                            Spacer(Modifier.height(16.dp))
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
    AnimatedSwipeDismiss(
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
                            style = MaterialTheme.typography.body2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.size(8.dp))

                        Text(
                            text = totalTask,
                            style = MaterialTheme.typography.body2
                        )
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
