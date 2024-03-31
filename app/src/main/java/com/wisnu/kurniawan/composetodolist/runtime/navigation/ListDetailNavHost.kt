package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.CreateListEditor
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ListDetailScreen
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ListDetailViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.TaskEditor
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.UpdateListEditor

fun NavGraphBuilder.ListDetailNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>,
    backIcon: ImageVector
) {
    // navController.navigate to ListDetailFlow.Root.route will crash due to not found
    // add "?$ARG_LIST_ID={$ARG_LIST_ID}" in startDestination for workaround
    navigation(
        startDestination = ListDetailFlow.ListDetailScreen.route,
        route = ListDetailFlow.Root.route
    ) {
        composable(
            route = ListDetailFlow.ListDetailScreen.route,
            arguments = ListDetailFlow.ListDetailScreen.arguments
        ) {
            val viewModel = hiltViewModel<ListDetailViewModel>()
            ListDetailScreen(
                viewModel = viewModel,
                backIcon = backIcon,
                showCreateListScreen = {
                    navController.navigate(ListDetailFlow.CreateList.route)
                },
                onCloseScreen = {
                    navController.navigateUp()
                },
                onRelaunchScreen = {
                    navController.navigate(ListDetailFlow.Root.route(it)) {
                        popUpTo(HomeFlow.DashboardScreen.route)
                    }
                },
                onClickSave = { navController.navigate(ListDetailFlow.UpdateList.route) },
                onClickBack = { navController.navigateUp() },
                onAddTaskClick = { navController.navigate(ListDetailFlow.CreateTask.route) },
                onTaskItemClick = { taskId, listId -> navController.navigate(StepFlow.Root.route(taskId, listId)) }
            )
        }
        ListDetailBottomSheetNavHost(
            navController = navController,
            bottomSheetConfig = bottomSheetConfig
        )
    }
}

private fun NavGraphBuilder.ListDetailBottomSheetNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    bottomSheet(ListDetailFlow.CreateList.route) {
        val viewModel = if (navController.previousBackStackEntry != null) {
            hiltViewModel<ListDetailViewModel>(
                navController.previousBackStackEntry!!
            )
        } else {
            hiltViewModel()
        }
        bottomSheetConfig.value = DefaultMainBottomSheetConfig
        CreateListEditor(
            viewModel = viewModel,
            onCancelClick = { navController.navigateUp() },
            onClosePage = { navController.navigateUp() },
            onSaveClick = { navController.navigateUp() },
        )
    }
    bottomSheet(ListDetailFlow.UpdateList.route) {
        val viewModel = if (navController.previousBackStackEntry != null) {
            hiltViewModel<ListDetailViewModel>(
                navController.previousBackStackEntry!!
            )
        } else {
            hiltViewModel()
        }
        bottomSheetConfig.value = DefaultMainBottomSheetConfig
        UpdateListEditor(
            viewModel = viewModel,
            onCancelClick = { navController.navigateUp() },
            onSaveClick = { navController.navigateUp() },
        )
    }
    bottomSheet(ListDetailFlow.CreateTask.route) {
        val viewModel = if (navController.previousBackStackEntry != null) {
            hiltViewModel<ListDetailViewModel>(
                navController.previousBackStackEntry!!
            )
        } else {
            hiltViewModel()
        }
        bottomSheetConfig.value = NoScrimSmallShapeMainBottomSheetConfig
        TaskEditor(
            viewModel = viewModel
        )
    }
}
