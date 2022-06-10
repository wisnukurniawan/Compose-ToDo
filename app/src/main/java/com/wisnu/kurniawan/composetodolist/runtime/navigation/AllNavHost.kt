package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllScreen
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllTabletScreen
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllViewModel

fun NavGraphBuilder.AllNavHost(
    navController: NavHostController,
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
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

fun NavGraphBuilder.AllTabletNavHost(
    navController: NavHostController,
) {
    navigation(
        startDestination = AllFlow.AllScreen.route,
        route = AllFlow.Root.route
    ) {
        composable(
            route = AllFlow.AllScreen.route
        ) {
            val viewModel = hiltViewModel<AllViewModel>()
            AllTabletScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
