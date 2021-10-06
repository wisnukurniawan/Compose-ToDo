package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainAction
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainScreen
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainViewModel
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTextField
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTransparentFooter
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeSearch
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeSearchValue
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.rememberSwipeSearchState
import com.wisnu.kurniawan.composetodolist.runtime.navigation.HomeFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ListDetailFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.SettingFlow

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel,
    toDoMainViewModel: ToDoMainViewModel
) {
    val state by viewModel.state.collectAsState()
    val todoMainState by toDoMainViewModel.state.collectAsState()

    DashboardScreen(
        email = state.user.email,
        todoData = todoMainState.data,
        currentDate = todoMainState.currentDate,
        scheduledTodayTaskCount = todoMainState.scheduledTodayTaskCount,
        scheduledTaskCount = todoMainState.scheduledTaskCount,
        allTaskCount = todoMainState.allTaskCount,
        onSettingClick = { navController.navigate(SettingFlow.Root.route) },
        onAddNewListClick = { navController.navigate(ListDetailFlow.Root.route()) },
        onAddNewGroupClick = { navController.navigate(HomeFlow.CreateGroup.route) },
        onClickGroup = { navController.navigate(HomeFlow.GroupMenu.route(it.group.id)) },
        onClickList = { navController.navigate(ListDetailFlow.Root.route(it.list.id)) },
        onSwipeToDelete = { toDoMainViewModel.dispatch(ToDoMainAction.DeleteList(it)) },
        onScheduledTodayTask = {},
        onScheduledTask = {},
        onClickAllTask = {},
    )
}

@Composable
fun DashboardScreen(
    email: String,
    todoData: List<ItemMainState>,
    currentDate: String,
    scheduledTodayTaskCount: String,
    scheduledTaskCount: String,
    allTaskCount: String,
    onSettingClick: () -> Unit,
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    onClickGroup: (ItemMainState.ItemGroup) -> Unit,
    onClickList: (ItemMainState.ItemListType) -> Unit,
    onSwipeToDelete: (ItemMainState.ItemListType) -> Unit,
    onScheduledTodayTask: () -> Unit,
    onScheduledTask: () -> Unit,
    onClickAllTask: () -> Unit,
) {
    var swipeSearchValue by remember { mutableStateOf(SwipeSearchValue.Closed) }
    val swipeSearchState = rememberSwipeSearchState(swipeSearchValue)

    val focusManager = LocalFocusManager.current
    val focusRequest = remember { FocusRequester() }

    LaunchedEffect(swipeSearchState.currentValue) {
        if (swipeSearchState.currentValue == SwipeSearchValue.Opened) {
            focusRequest.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    val closeSearch = { swipeSearchValue = SwipeSearchValue.Closed }
    if (swipeSearchState.currentValue == SwipeSearchValue.Opened) {
        BackHandler(onBack = closeSearch)
    }

    SwipeSearch(
        state = swipeSearchState,
        onFling = {
            swipeSearchValue = it
        },
        onSearchAreaClick = closeSearch,
        content = {
            DashboardContent(
                email,
                todoData,
                currentDate,
                scheduledTodayTaskCount,
                scheduledTaskCount,
                allTaskCount,
                onClickGroup,
                onClickList,
                onSwipeToDelete,
                onScheduledTodayTask,
                onScheduledTask,
                onClickAllTask,
                onSettingClick,
                onAddNewListClick,
                onAddNewGroupClick
            )
        },
        searchSection = {
            SearchSection(
                modifier = Modifier.weight(1f),
                focusRequester = focusRequest,
                onCancelClick = closeSearch
            )
        },
        searchBody = {

        }
    )
}

@Composable
private fun SearchSection(
    modifier: Modifier,
    focusRequester: FocusRequester,
    onCancelClick: () -> Unit
) {
    PgTextField(
        value = "",
        onValueChange = { },
        placeholderValue = stringResource(R.string.todo_search),
        modifier = modifier
            .height(50.dp)
            .focusRequester(focusRequester),
        shape = MaterialTheme.shapes.large,
        textStyle = MaterialTheme.typography.subtitle2,
        leadingIcon = {
            PgIcon(
                imageVector = Icons.Rounded.Search
            )
        }
    )

    Spacer(Modifier.width(8.dp))

    Column {
        TextButton(
            onClick = onCancelClick,
            shape = CircleShape,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface)
        ) {
            Text(
                text = stringResource(R.string.todo_cancel),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
            )
        }

        Spacer(Modifier.height(6.dp))
    }
}

@Composable
private fun DashboardContent(
    email: String,
    todoData: List<ItemMainState>,
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
    onSettingClick: () -> Unit,
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit
) {
    PgPageLayout {
        Text(
            text = email,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        Box(modifier = Modifier.fillMaxSize().weight(1F)) {
            ToDoMainScreen(
                todoData,
                currentDate,
                scheduledTodayTaskCount,
                scheduledTaskCount,
                allTaskCount,
                onClickGroup,
                onClickList,
                onSwipeToDelete,
                onScheduledTodayTask,
                onScheduledTask,
                onClickAllTask
            )

            Footer(
                onSettingClick = onSettingClick,
                onAddNewListClick = onAddNewListClick,
                onAddNewGroupClick = onAddNewGroupClick,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }

        Spacer(Modifier.height(36.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Footer(
    onSettingClick: () -> Unit,
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PgTransparentFooter(modifier) {
        PgIconButton(onClick = onSettingClick) {
            PgIcon(imageVector = Icons.Rounded.Settings)
        }

        Surface(
            modifier = Modifier.height(48.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colors.primary,
            onClick = onAddNewListClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp, end = 16.dp)
            ) {
                PgIcon(
                    imageVector = Icons.Rounded.Add,
                    tint = Color.White
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.todo_new_list),
                    style = MaterialTheme.typography.button,
                    color = Color.White
                )
            }
        }

        PgIconButton(onClick = onAddNewGroupClick) {
            PgIcon(imageVector = Icons.Rounded.NoteAdd)
        }
    }
}



