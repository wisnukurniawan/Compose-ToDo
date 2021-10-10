package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CreateNewFolder
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainAction
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainScreen
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchAction
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchWidget
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTransparentFooter
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeSearch
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeSearchValue
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.rememberSwipeSearchState
import com.wisnu.kurniawan.composetodolist.runtime.navigation.AllFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.HomeFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ListDetailFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.MainFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ScheduledFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ScheduledTodayFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.SettingFlow

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel,
    toDoMainViewModel: ToDoMainViewModel,
    searchViewModel: SearchViewModel,
) {
    val state by viewModel.state.collectAsState()
    val todoMainState by toDoMainViewModel.state.collectAsState()
    val searchState by searchViewModel.state.collectAsState()

    DashboardScreen(
        email = state.user.email,
        searchText = searchState.searchText,
        todoData = todoMainState.data,
        currentDate = todoMainState.currentDate,
        scheduledTodayTaskCount = todoMainState.scheduledTodayTaskCount,
        scheduledTaskCount = todoMainState.scheduledTaskCount,
        allTaskCount = todoMainState.allTaskCount,
        onSearchChange = { searchViewModel.dispatch(SearchAction.ChangeSearchText(it)) },
        onSearchOpened = { searchViewModel.dispatch(SearchAction.OnShow) },
        onSettingClick = { navController.navigate(SettingFlow.Root.route) },
        onAddNewListClick = { navController.navigate(ListDetailFlow.Root.route()) },
        onAddNewGroupClick = { navController.navigate(HomeFlow.CreateGroup.route) },
        onClickGroup = { navController.navigate(HomeFlow.GroupMenu.route(it.group.id)) },
        onClickList = { navController.navigate(ListDetailFlow.Root.route(it.list.id)) },
        onSwipeToDelete = { toDoMainViewModel.dispatch(ToDoMainAction.DeleteList(it)) },
        onClickScheduledTodayTask = { navController.navigate(ScheduledTodayFlow.Root.route()) },
        onClickScheduledTask = { navController.navigate(ScheduledFlow.Root.route()) },
        onClickAllTask = { navController.navigate(AllFlow.Root.route) },
    )
}

@Composable
fun DashboardTabletScreen(
    navController: NavController,
    navControllerLeft: NavController,
    navControllerRight: NavController,
    viewModel: DashboardViewModel,
    toDoMainViewModel: ToDoMainViewModel
) {
    val state by viewModel.state.collectAsState()
    val todoMainState by toDoMainViewModel.state.collectAsState()

    DashboardContent(
        email = state.user.email,
        todoData = todoMainState.data,
        currentDate = todoMainState.currentDate,
        scheduledTodayTaskCount = todoMainState.scheduledTodayTaskCount,
        scheduledTaskCount = todoMainState.scheduledTaskCount,
        allTaskCount = todoMainState.allTaskCount,
        onSettingClick = { navController.navigate(SettingFlow.Root.route) },
        onAddNewListClick = {
            navControllerRight.navigate(ListDetailFlow.Root.route()) {
                popUpTo(MainFlow.RootEmpty.route)
            }
        },
        onAddNewGroupClick = { navControllerLeft.navigate(HomeFlow.CreateGroup.route) },
        onClickGroup = { navControllerLeft.navigate(HomeFlow.GroupMenu.route(it.group.id)) },
        onClickList = {
            navControllerRight.navigate(ListDetailFlow.Root.route(it.list.id)) {
                popUpTo(MainFlow.RootEmpty.route)
            }
        },
        onSwipeToDelete = { toDoMainViewModel.dispatch(ToDoMainAction.DeleteList(it)) },
        onClickScheduledTodayTask = {
            navControllerRight.navigate(ScheduledTodayFlow.Root.route()) {
                popUpTo(MainFlow.RootEmpty.route)
            }
        },
        onClickScheduledTask = {
            navControllerRight.navigate(ScheduledFlow.Root.route()) {
                popUpTo(MainFlow.RootEmpty.route)
            }
        },
        onClickAllTask = {
            navControllerRight.navigate(AllFlow.Root.route) {
                popUpTo(MainFlow.RootEmpty.route)
            }
        },
        onClickSearch = {}
    )
}

@Composable
private fun DashboardScreen(
    email: String,
    searchText: TextFieldValue,
    todoData: List<ItemMainState>,
    currentDate: String,
    scheduledTodayTaskCount: String,
    scheduledTaskCount: String,
    allTaskCount: String,
    onSettingClick: () -> Unit,
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    onSearchChange: (TextFieldValue) -> Unit,
    onSearchOpened: () -> Unit,
    onClickGroup: (ItemMainState.ItemGroup) -> Unit,
    onClickList: (ItemMainState.ItemListType) -> Unit,
    onSwipeToDelete: (ItemMainState.ItemListType) -> Unit,
    onClickScheduledTodayTask: () -> Unit,
    onClickScheduledTask: () -> Unit,
    onClickAllTask: () -> Unit,
) {
    var swipeSearchValue by remember { mutableStateOf(SwipeSearchValue.Closed) }
    val swipeSearchState = rememberSwipeSearchState(swipeSearchValue)

    val focusManager = LocalFocusManager.current
    val focusRequest = remember { FocusRequester() }
    LaunchedEffect(swipeSearchState.currentValue) {
        if (swipeSearchState.currentValue == SwipeSearchValue.Opened) {
            onSearchOpened()
            focusRequest.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    val closeSearch = { swipeSearchValue = SwipeSearchValue.Closed }
    val openSearch = { swipeSearchValue = SwipeSearchValue.Opened }
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
                onClickScheduledTodayTask,
                onClickScheduledTask,
                onClickAllTask,
                onSettingClick,
                openSearch,
                onAddNewListClick,
                onAddNewGroupClick
            )
        },
        searchSection = {
            SearchWidget(
                searchText = searchText,
                modifier = Modifier.weight(1f),
                focusRequester = focusRequest,
                onCancelClick = closeSearch,
                onSearchChange = onSearchChange
            )
        },
        searchBody = {

        }
    )
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
    onClickScheduledTodayTask: () -> Unit,
    onClickScheduledTask: () -> Unit,
    onClickAllTask: () -> Unit,
    onSettingClick: () -> Unit,
    onClickSearch: () -> Unit,
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit
) {
    PgPageLayout {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PgIconButton(onClick = onSettingClick, color = Color.Transparent) {
                    PgIcon(imageVector = Icons.Rounded.Menu)
                }

                Spacer(Modifier.width(16.dp))

                Text(
                    text = email,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            PgIconButton(onClick = onClickSearch, color = Color.Transparent) {
                PgIcon(imageVector = Icons.Rounded.Search)
            }

        }

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
                onClickScheduledTodayTask,
                onClickScheduledTask,
                onClickAllTask
            )

            Footer(
                onAddNewListClick = onAddNewListClick,
                onAddNewGroupClick = onAddNewGroupClick,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Footer(
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PgTransparentFooter(modifier) {
        Surface(
            modifier = Modifier.height(48.dp).weight(1f),
            shape = MaterialTheme.shapes.large,
            color = Color.Transparent,
            onClick = onAddNewListClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp, end = 16.dp)
            ) {
                PgIcon(
                    imageVector = Icons.Rounded.Add,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.todo_new_list),
                    style = MaterialTheme.typography.button,
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        PgIconButton(onClick = onAddNewGroupClick, color = Color.Transparent) {
            PgIcon(imageVector = Icons.Rounded.CreateNewFolder)
        }
    }
}



