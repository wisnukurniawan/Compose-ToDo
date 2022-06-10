package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ScheduledScreen
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ScheduledTabletScreen
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ScheduledViewModel

fun NavGraphBuilder.ScheduledNavHost(
    navController: NavHostController,
) {
    navigation(
        startDestination = ScheduledFlow.ScheduledScreen.route,
        route = ScheduledFlow.Root.route
    ) {
        composable(
            route = ScheduledFlow.ScheduledScreen.route,
            arguments = ScheduledFlow.ScheduledScreen.arguments
        ) {
            val viewModel = hiltViewModel<ScheduledViewModel>()
            ScheduledScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

fun NavGraphBuilder.ScheduledTabletNavHost(
    navController: NavHostController,
) {
    navigation(
        startDestination = ScheduledFlow.ScheduledScreen.route,
        route = ScheduledFlow.Root.route
    ) {
        composable(
            route = ScheduledFlow.ScheduledScreen.route,
            arguments = ScheduledFlow.ScheduledScreen.arguments
        ) {
            val viewModel = hiltViewModel<ScheduledViewModel>()
            ScheduledTabletScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
