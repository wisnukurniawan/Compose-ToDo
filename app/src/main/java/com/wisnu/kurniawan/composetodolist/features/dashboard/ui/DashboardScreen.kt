package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CreateNewFolder
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.ItemAllState
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainAction
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainScreen
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchAction
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchContent
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchWidget
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTransparentFooter
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeSearch
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeSearchValue
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.rememberSwipeSearchState
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    toDoMainViewModel: ToDoMainViewModel,
    searchViewModel: SearchViewModel,
    onSettingClick: () -> Unit,
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    onClickGroup: (String) -> Unit,
    onClickList: (String) -> Unit,
    onClickScheduledTodayTask: () -> Unit,
    onClickScheduledTask: () -> Unit,
    onClickAllTask: () -> Unit,
    onSearchTaskItemClick: (String, String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val todoMainState by toDoMainViewModel.state.collectAsStateWithLifecycle()
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()

    DashboardScreen(
        email = state.user.email,
        searchText = searchState.searchText,
        todoData = todoMainState.items,
        currentDate = todoMainState.currentDate,
        scheduledTodayTaskCount = todoMainState.scheduledTodayTaskCount,
        scheduledTaskCount = todoMainState.scheduledTaskCount,
        allTaskCount = todoMainState.allTaskCount,
        isAllTaskSelected = todoMainState.isAllTaskSelected,
        isScheduledTodayTaskSelected = todoMainState.isScheduledTodayTaskSelected,
        isScheduledTaskSelected = todoMainState.isScheduledTaskSelected,
        searchResultItems = searchState.items,
        onSearchChange = { searchViewModel.dispatch(SearchAction.ChangeSearchText(it)) },
        onSearchClosed = { searchViewModel.dispatch(SearchAction.ChangeSearchText(TextFieldValue())) },
        onSettingClick = onSettingClick,
        onAddNewListClick = onAddNewListClick,
        onAddNewGroupClick = onAddNewGroupClick,
        onClickGroup = { onClickGroup(it.group.id) },
        onClickList = { onClickList(it.list.id) },
        onSwipeToDelete = { toDoMainViewModel.dispatch(ToDoMainAction.DeleteList(it)) },
        onClickScheduledTodayTask = onClickScheduledTodayTask,
        onClickScheduledTask = onClickScheduledTask,
        onClickAllTask = onClickAllTask,
        onSearchTaskItemClick = { onSearchTaskItemClick(it.task.id, it.list.id) },
        onSearchTaskStatusItemClick = { searchViewModel.dispatch(SearchAction.TaskAction.OnToggleStatus(it)) },
        onSearchTaskSwipeToDelete = { searchViewModel.dispatch(SearchAction.TaskAction.Delete(it)) },
    )
}

@Composable
fun DashboardTabletScreen(
    navBackStackEntry: NavBackStackEntry?,
    viewModel: DashboardViewModel,
    toDoMainViewModel: ToDoMainViewModel,
    onSettingClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    onClickGroup: (String) -> Unit,
    onAddNewListClick: () -> Unit,
    onClickList: (String) -> Unit,
    onClickScheduledTodayTask: () -> Unit,
    onClickScheduledTask: () -> Unit,
    onClickAllTask: () -> Unit,
    onClickSearch: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val todoMainState by toDoMainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(navBackStackEntry) {
        toDoMainViewModel.dispatch(ToDoMainAction.NavBackStackEntryChanged(navBackStackEntry?.destination?.route, navBackStackEntry?.arguments))
    }

    DashboardContent(
        email = state.user.email,
        todoData = todoMainState.items,
        currentDate = todoMainState.currentDate,
        scheduledTodayTaskCount = todoMainState.scheduledTodayTaskCount,
        scheduledTaskCount = todoMainState.scheduledTaskCount,
        allTaskCount = todoMainState.allTaskCount,
        isAllTaskSelected = todoMainState.isAllTaskSelected,
        isScheduledTodayTaskSelected = todoMainState.isScheduledTodayTaskSelected,
        isScheduledTaskSelected = todoMainState.isScheduledTaskSelected,
        onSettingClick = onSettingClick,
        onAddNewListClick = onAddNewListClick,
        onAddNewGroupClick = onAddNewGroupClick,
        onClickGroup = { onClickGroup(it.group.id) },
        onClickList = {
            onClickList(it.list.id)
        },
        onSwipeToDelete = { toDoMainViewModel.dispatch(ToDoMainAction.DeleteList(it)) },
        onClickScheduledTodayTask = onClickScheduledTodayTask,
        onClickScheduledTask = onClickScheduledTask,
        onClickAllTask = onClickAllTask,
        onClickSearch = onClickSearch
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
    isAllTaskSelected: Boolean,
    isScheduledTodayTaskSelected: Boolean,
    isScheduledTaskSelected: Boolean,
    searchResultItems: List<ItemAllState>,
    onSettingClick: () -> Unit,
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    onSearchChange: (TextFieldValue) -> Unit,
    onSearchClosed: () -> Unit,
    onClickGroup: (ItemMainState.ItemGroup) -> Unit,
    onClickList: (ItemMainState.ItemListType) -> Unit,
    onSwipeToDelete: (ItemMainState.ItemListType) -> Unit,
    onClickScheduledTodayTask: () -> Unit,
    onClickScheduledTask: () -> Unit,
    onClickAllTask: () -> Unit,
    onSearchTaskItemClick: (ItemAllState.Task) -> Unit,
    onSearchTaskStatusItemClick: (ToDoTask) -> Unit,
    onSearchTaskSwipeToDelete: (ToDoTask) -> Unit
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
            onSearchClosed()
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
                email = email,
                todoData = todoData,
                currentDate = currentDate,
                scheduledTodayTaskCount = scheduledTodayTaskCount,
                scheduledTaskCount = scheduledTaskCount,
                allTaskCount = allTaskCount,
                isAllTaskSelected = isAllTaskSelected,
                isScheduledTodayTaskSelected = isScheduledTodayTaskSelected,
                isScheduledTaskSelected = isScheduledTaskSelected,
                onClickGroup = onClickGroup,
                onClickList = onClickList,
                onSwipeToDelete = onSwipeToDelete,
                onClickScheduledTodayTask = onClickScheduledTodayTask,
                onClickScheduledTask = onClickScheduledTask,
                onClickAllTask = onClickAllTask,
                onSettingClick = onSettingClick,
                onClickSearch = openSearch,
                onAddNewListClick = onAddNewListClick,
                onAddNewGroupClick = onAddNewGroupClick
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
            SearchContent(
                items = searchResultItems,
                onTaskItemClick = onSearchTaskItemClick,
                onTaskStatusItemClick = onSearchTaskStatusItemClick,
                onTaskSwipeToDelete = onSearchTaskSwipeToDelete,
            )
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
    isAllTaskSelected: Boolean,
    isScheduledTodayTaskSelected: Boolean,
    isScheduledTaskSelected: Boolean,
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
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            PgIconButton(onClick = onClickSearch, color = Color.Transparent) {
                PgIcon(imageVector = Icons.Rounded.Search)
            }

        }

        Box(modifier = Modifier.fillMaxSize().weight(1F)) {
            ToDoMainScreen(
                data = todoData,
                currentDate = currentDate,
                scheduledTodayTaskCount = scheduledTodayTaskCount,
                scheduledTaskCount = scheduledTaskCount,
                allTaskCount = allTaskCount,
                isAllTaskSelected = isAllTaskSelected,
                isScheduledTodayTaskSelected = isScheduledTodayTaskSelected,
                isScheduledTaskSelected = isScheduledTaskSelected,
                onClickGroup = onClickGroup,
                onClickList = onClickList,
                onSwipeToDelete = onSwipeToDelete,
                onClickScheduledTodayTask = onClickScheduledTodayTask,
                onClickScheduledTask = onClickScheduledTask,
                onClickAllTask = onClickAllTask,
            )

            Footer(
                onAddNewListClick = onAddNewListClick,
                onAddNewGroupClick = onAddNewGroupClick,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
private fun Footer(
    onAddNewListClick: () -> Unit,
    onAddNewGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PgTransparentFooter(modifier) {
        val shape = MaterialTheme.shapes.large
        Surface(
            modifier = Modifier
                .height(48.dp)
                .weight(1f)
                .clip(shape)
                .clickable(onClick = onAddNewListClick),
            shape = shape,
            color = Color.Transparent,
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
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        PgIconButton(onClick = onAddNewGroupClick, color = Color.Transparent) {
            PgIcon(imageVector = Icons.Rounded.CreateNewFolder)
        }
    }
}



