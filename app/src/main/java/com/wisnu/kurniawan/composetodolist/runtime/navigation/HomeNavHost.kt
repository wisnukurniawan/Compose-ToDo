package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.wisnu.kurniawan.composetodolist.features.dashboard.ui.DashboardScreen
import com.wisnu.kurniawan.composetodolist.features.dashboard.ui.DashboardTabletScreen
import com.wisnu.kurniawan.composetodolist.features.dashboard.ui.DashboardViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.group.ui.CreateGroupScreen
import com.wisnu.kurniawan.composetodolist.features.todo.group.ui.CreateGroupViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.group.ui.UpdateGroupScreen
import com.wisnu.kurniawan.composetodolist.features.todo.grouplist.ui.EditGroupListScreen
import com.wisnu.kurniawan.composetodolist.features.todo.grouplist.ui.UpdateGroupListScreen
import com.wisnu.kurniawan.composetodolist.features.todo.grouplist.ui.UpdateGroupListViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.ui.GroupMenuScreen
import com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.ui.GroupMenuViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ToDoMainViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchViewModel

fun NavGraphBuilder.HomeNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    navigation(startDestination = HomeFlow.DashboardScreen.route, route = HomeFlow.Root.route) {
        composable(HomeFlow.DashboardScreen.route) {
            val viewModel = hiltViewModel<DashboardViewModel>()
            val toDoMainViewModel = hiltViewModel<ToDoMainViewModel>()
            val searchViewModel = hiltViewModel<SearchViewModel>()
            DashboardScreen(
                viewModel = viewModel,
                toDoMainViewModel = toDoMainViewModel,
                searchViewModel = searchViewModel,
                onSettingClick = { navController.navigate(SettingFlow.Root.route) },
                onAddNewListClick = { navController.navigate(ListDetailFlow.Root.route()) },
                onAddNewGroupClick = { navController.navigate(HomeFlow.CreateGroup.route) },
                onClickGroup = { navController.navigate(HomeFlow.GroupMenu.route(it)) },
                onClickList = { navController.navigate(ListDetailFlow.Root.route(it)) },
                onClickScheduledTodayTask = { navController.navigate(ScheduledTodayFlow.Root.route()) },
                onClickScheduledTask = { navController.navigate(ScheduledFlow.Root.route()) },
                onClickAllTask = { navController.navigate(AllFlow.Root.route) },
                onSearchTaskItemClick = { taskId, listId -> navController.navigate(StepFlow.Root.route(taskId, listId)) }
            )
        }
        HomeBottomSheetNavHost(
            bottomSheetConfig = bottomSheetConfig,
            navController = navController
        )
    }
}

fun NavGraphBuilder.HomeTabletNavHost(
    navController: NavHostController,
    navControllerLeft: NavHostController,
    navControllerRight: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    navigation(startDestination = HomeFlow.DashboardScreen.route, route = HomeFlow.Root.route) {
        composable(HomeFlow.DashboardScreen.route) {
            val viewModel = hiltViewModel<DashboardViewModel>()
            val toDoMainViewModel = hiltViewModel<ToDoMainViewModel>()
            val navBackStackEntry by navControllerRight.currentBackStackEntryAsState()

            DashboardTabletScreen(
                navBackStackEntry = navBackStackEntry,
                viewModel = viewModel,
                toDoMainViewModel = toDoMainViewModel,
                onSettingClick = { navController.navigate(SettingFlow.Root.route) },
                onAddNewGroupClick = { navControllerLeft.navigate(HomeFlow.CreateGroup.route) },
                onClickGroup = { navControllerLeft.navigate(HomeFlow.GroupMenu.route(it)) },
                onAddNewListClick = {
                    navControllerRight.navigate(ListDetailFlow.Root.route()) {
                        popUpTo(MainFlow.RootEmpty.route)
                    }
                },
                onClickList = {
                    navControllerRight.navigate(ListDetailFlow.Root.route(it)) {
                        popUpTo(MainFlow.RootEmpty.route)
                    }
                },
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
                onClickSearch = {
                    navControllerRight.navigate(SearchFlow.Root.route) {
                        popUpTo(MainFlow.RootEmpty.route)
                    }
                }
            )
        }

        HomeBottomSheetNavHost(
            bottomSheetConfig = bottomSheetConfig,
            navController = navControllerLeft
        )
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.HomeBottomSheetNavHost(
    bottomSheetConfig: MutableState<MainBottomSheetConfig>,
    navController: NavHostController
) {
    bottomSheet(
        route = HomeFlow.GroupMenu.route,
        arguments = HomeFlow.GroupMenu.arguments
    ) {
        val viewModel = hiltViewModel<GroupMenuViewModel>()
        bottomSheetConfig.value = DefaultMainBottomSheetConfig
        GroupMenuScreen(
            viewModel = viewModel,
            onAddRemoveClick = {
                navController.navigate(HomeFlow.EditGroupList.route(viewModel.groupId))
            },
            onDeleteClick = {
                navController.navigateUp()
            },
            onRenameClick = {
                navController.navigate(HomeFlow.UpdateGroup.route(viewModel.groupId))
            }
        )
    }
    bottomSheet(HomeFlow.CreateGroup.route) {
        val viewModel = hiltViewModel<CreateGroupViewModel>()
        bottomSheetConfig.value = DefaultMainBottomSheetConfig
        CreateGroupScreen(
            viewModel = viewModel,
            onHideScreen = { navController.navigateUp() },
            onShowGroupListScreen = {
                navController.navigateUp()
                navController.navigate(HomeFlow.UpdateGroupList.route(it))
            },
            onCancelClick = { navController.navigateUp() }
        )
    }
    bottomSheet(
        route = HomeFlow.UpdateGroup.route,
        arguments = HomeFlow.UpdateGroup.arguments
    ) {
        val viewModel = hiltViewModel<CreateGroupViewModel>()
        bottomSheetConfig.value = DefaultMainBottomSheetConfig
        UpdateGroupScreen(
            viewModel = viewModel,
            onCancelClick = { navController.navigateUp() },
            onHideScreen = { navController.navigateUp() },
            onClickBack = { navController.navigateUp() },
        )
    }
    bottomSheet(
        route = HomeFlow.UpdateGroupList.route,
        arguments = HomeFlow.UpdateGroupList.arguments
    ) {
        val viewModel = hiltViewModel<UpdateGroupListViewModel>()
        bottomSheetConfig.value = DefaultMainBottomSheetConfig
        UpdateGroupListScreen(
            viewModel = viewModel,
            onSkip = { navController.navigateUp() },
            onSubmit = { navController.navigateUp() },
        )
    }
    bottomSheet(
        route = HomeFlow.EditGroupList.route,
        arguments = HomeFlow.EditGroupList.arguments
    ) {
        val viewModel = hiltViewModel<UpdateGroupListViewModel>()
        bottomSheetConfig.value = DefaultMainBottomSheetConfig
        EditGroupListScreen(
            viewModel = viewModel,
            onSubmit = { navController.navigateUp() },
            onSkip = { navController.navigateUp() },
            onClickBack = { navController.navigateUp() },
        )
    }
}
