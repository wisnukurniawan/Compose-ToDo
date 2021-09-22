package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.wisnu.kurniawan.composetodolist.features.dashboard.ui.DashboardScreen
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
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.bottomSheet
import com.wisnu.kurniawan.composetodolist.runtime.MainBottomSheetConfig
import com.wisnu.kurniawan.composetodolist.runtime.defaultMainBottomSheetConfig

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.HomeNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    navigation(startDestination = HomeFlow.DashboardScreen.route, route = HomeFlow.Root.route) {
        composable(HomeFlow.DashboardScreen.route) {
            val viewModel = hiltViewModel<DashboardViewModel>()
            val toDoMainViewModel = hiltViewModel<ToDoMainViewModel>()
            DashboardScreen(
                navController = navController,
                viewModel = viewModel,
                toDoMainViewModel = toDoMainViewModel
            )
        }
        bottomSheet(
            route = HomeFlow.GroupMenu.route + "?$ARG_GROUP_ID={$ARG_GROUP_ID}",
            arguments = listOf(
                navArgument(ARG_GROUP_ID) {
                    defaultValue = ""
                }
            )
        ) {
            val viewModel = hiltViewModel<GroupMenuViewModel>()
            bottomSheetConfig.value = defaultMainBottomSheetConfig
            GroupMenuScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        bottomSheet(HomeFlow.CreateGroup.route) {
            val viewModel = hiltViewModel<CreateGroupViewModel>()
            bottomSheetConfig.value = defaultMainBottomSheetConfig
            CreateGroupScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        bottomSheet(
            route = HomeFlow.UpdateGroup.route + "?$ARG_GROUP_ID={$ARG_GROUP_ID}",
            arguments = listOf(
                navArgument(ARG_GROUP_ID) {
                    defaultValue = ""
                }
            )
        ) {
            val viewModel = hiltViewModel<CreateGroupViewModel>()
            bottomSheetConfig.value = defaultMainBottomSheetConfig
            UpdateGroupScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        bottomSheet(
            route = HomeFlow.UpdateGroupList.route + "?$ARG_GROUP_ID={$ARG_GROUP_ID}",
            arguments = listOf(
                navArgument(ARG_GROUP_ID) {
                    defaultValue = ""
                }
            )
        ) {
            val viewModel = hiltViewModel<UpdateGroupListViewModel>()
            bottomSheetConfig.value = defaultMainBottomSheetConfig
            UpdateGroupListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        bottomSheet(
            route = HomeFlow.EditGroupList.route + "?$ARG_GROUP_ID={$ARG_GROUP_ID}",
            arguments = listOf(
                navArgument(ARG_GROUP_ID) {
                    defaultValue = ""
                }
            )
        ) {
            val viewModel = hiltViewModel<UpdateGroupListViewModel>()
            bottomSheetConfig.value = defaultMainBottomSheetConfig
            EditGroupListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
