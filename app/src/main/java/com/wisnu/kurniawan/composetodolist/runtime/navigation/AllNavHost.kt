package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllScreen
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllTabletScreen
import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.AllViewModel

@OptIn(ExperimentalMaterialNavigationApi::class)
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

@OptIn(ExperimentalMaterialNavigationApi::class)
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
