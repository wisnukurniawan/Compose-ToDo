package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllScreen
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllViewModel

fun NavGraphBuilder.AllNavHost(
    navController: NavHostController,
    backIcon: ImageVector
) {
    navigation(
        startDestination = AllFlow.AllScreen.route,
        route = AllFlow.Root.route
    ) {
        composable(
            route = AllFlow.AllScreen.route
        ) {
            val viewModel = hiltViewModel<AllViewModel>()
            AllScreen(
                viewModel = viewModel,
                backIcon = backIcon,
                onClickBack = { navController.navigateUp() },
                onTaskItemClick = { taskId, listId -> navController.navigate(StepFlow.Root.route(taskId, listId)) }
            )
        }

    }
}
