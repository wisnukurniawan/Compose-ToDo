package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchScreen
import com.wisnu.kurniawan.composetodolist.features.todo.search.ui.SearchViewModel

fun NavGraphBuilder.SearchNavHost(
    navController: NavHostController,
) {
    navigation(startDestination = SearchFlow.SearchScreen.route, route = SearchFlow.Root.route) {
        composable(
            route = SearchFlow.SearchScreen.route
        ) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
