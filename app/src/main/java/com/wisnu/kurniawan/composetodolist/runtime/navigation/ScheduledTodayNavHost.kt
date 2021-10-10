package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ScheduledTodayScreen
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ScheduledTodayTabletScreen
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ScheduledViewModel

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.ScheduledTodayNavHost(
    navController: NavHostController,
) {
    navigation(
        startDestination = ScheduledTodayFlow.ScheduledTodayScreen.route,
        route = ScheduledTodayFlow.Root.route
    ) {
        composable(
            route = ScheduledTodayFlow.ScheduledTodayScreen.route,
            arguments = ScheduledTodayFlow.ScheduledTodayScreen.arguments
        ) {
            val viewModel = hiltViewModel<ScheduledViewModel>()
            ScheduledTodayScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.ScheduledTodayTabletNavHost(
    navController: NavHostController,
) {
    navigation(
        startDestination = ScheduledTodayFlow.ScheduledTodayScreen.route,
        route = ScheduledTodayFlow.Root.route
    ) {
        composable(
            route = ScheduledTodayFlow.ScheduledTodayScreen.route,
            arguments = ScheduledTodayFlow.ScheduledTodayScreen.arguments
        ) {
            val viewModel = hiltViewModel<ScheduledViewModel>()
            ScheduledTodayTabletScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
