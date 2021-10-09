//package com.wisnu.kurniawan.composetodolist.features.dashboard.ui
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ListDetailAction
//import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ListDetailContent
//import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ListDetailTitle
//import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ListDetailViewModel
//import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ToDoTaskItem
//import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainAction
//import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainViewModel
//import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchAction
//import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchViewModel
//import com.wisnu.kurniawan.composetodolist.foundation.extension.selectedColor
//import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalCloseButton
//import com.wisnu.kurniawan.composetodolist.model.ToDoTask
//import com.wisnu.kurniawan.composetodolist.runtime.navigation.HomeFlow
//import com.wisnu.kurniawan.composetodolist.runtime.navigation.ListDetailFlow
//import com.wisnu.kurniawan.composetodolist.runtime.navigation.SettingFlow
//import com.wisnu.kurniawan.composetodolist.runtime.navigation.StepFlow
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//

// EXPERIMENT

//@Composable
//fun DashboardTabletScreen(
//    navController: NavController,
//    viewModel: DashboardViewModel,
//    toDoMainViewModel: ToDoMainViewModel,
//    searchViewModel: SearchViewModel,
//    listDetailViewModel: ListDetailViewModel
//) {
//    val coroutineScope = rememberCoroutineScope()
//
//    val state by viewModel.state.collectAsState()
//    val todoMainState by toDoMainViewModel.state.collectAsState()
//    val searchState by searchViewModel.state.collectAsState()
//    val listDetailState by listDetailViewModel.state.collectAsState()
//
//    Row(modifier = Modifier.fillMaxSize()) {
//        DashboardScreen(
//            modifier = Modifier.weight(0.4F),
//            email = state.user.email,
//            searchText = searchState.searchText,
//            todoData = todoMainState.data,
//            currentDate = todoMainState.currentDate,
//            scheduledTodayTaskCount = todoMainState.scheduledTodayTaskCount,
//            scheduledTaskCount = todoMainState.scheduledTaskCount,
//            allTaskCount = todoMainState.allTaskCount,
//            onSearchChange = { searchViewModel.dispatch(SearchAction.ChangeSearchText(it)) },
//            onSearchOpened = { searchViewModel.dispatch(SearchAction.OnShow) },
//            onSettingClick = { navController.navigate(SettingFlow.Root.route) },
//            onAddNewListClick = {
//                coroutineScope.launch {
//                    listDetailViewModel.dispatch(ListDetailAction.Init())
//                    delay(100) // Hack to fix sheet not shown
//                    navController.navigate(ListDetailFlow.CreateList.route)
//                }
//            },
//            onAddNewGroupClick = { navController.navigate(HomeFlow.CreateGroup.route) },
//            onClickGroup = { navController.navigate(HomeFlow.GroupMenu.route(it.group.id)) },
//            onClickList = { listDetailViewModel.dispatch(ListDetailAction.Init(it.list.id)) },
//            onSwipeToDelete = { toDoMainViewModel.dispatch(ToDoMainAction.DeleteList(it)) },
//            onScheduledTodayTask = {
//                // replace right
//            },
//            onScheduledTask = {
//                // replace right
//            },
//            onClickAllTask = {
//                // replace right
//            },
//        )
//
//        Box(
//            modifier = Modifier
//                .fillMaxHeight()
//                .width(1.dp)
//                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
//        )
//
//        Box(
//            modifier = Modifier.weight(0.6F),
//        ) {
//            ListDetailTabletContent(
//                tasks = listDetailState.listDisplayable.tasks,
//                tempTaskName = listDetailState.taskName.text,
//                header = {
//                    ListDetailTitle(
//                        onClick = { navController.navigate(ListDetailFlow.UpdateList.route) },
//                        text = listDetailState.list.name,
//                        color = listDetailState.colors.selectedColor(),
//                        backContent = {
//                            PgModalCloseButton {
//                                listDetailViewModel.dispatch(ListDetailAction.Empty)
//                            }
//                        }
//                    )
//                },
//                color = listDetailState.colors.selectedColor(),
//                onAddTaskClick = { navController.navigate(ListDetailFlow.CreateTask.route) },
//                onTaskItemClick = { navController.navigate(StepFlow.Root.route(it.id, listDetailState.list.id)) },
//                onTaskStatusItemClick = { listDetailViewModel.dispatch(ListDetailAction.TaskAction.OnToggleStatus(it)) },
//                onTaskSwipeToDelete = { listDetailViewModel.dispatch(ListDetailAction.TaskAction.Delete(it)) },
//                shouldBlank = listDetailState.shouldBlank
//            )
//        }
//    }
//}
//
//@Composable
//private fun ListDetailTabletContent(
//    modifier: Modifier = Modifier,
//    tasks: List<ToDoTaskItem>,
//    tempTaskName: String,
//    header: @Composable ColumnScope.() -> Unit,
//    onAddTaskClick: () -> Unit,
//    onTaskItemClick: (ToDoTask) -> Unit,
//    onTaskStatusItemClick: (ToDoTask) -> Unit,
//    onTaskSwipeToDelete: (ToDoTask) -> Unit,
//    color: Color,
//    shouldBlank: Boolean
//) {
//    if (!shouldBlank) {
//        ListDetailContent(
//            modifier = modifier,
//            tasks = tasks,
//            tempTaskName = tempTaskName,
//            header = header,
//            color = color,
//            onAddTaskClick = onAddTaskClick,
//            onTaskItemClick = onTaskItemClick,
//            onTaskStatusItemClick = onTaskStatusItemClick,
//            onTaskSwipeToDelete = onTaskSwipeToDelete
//        )
//    }
//}
