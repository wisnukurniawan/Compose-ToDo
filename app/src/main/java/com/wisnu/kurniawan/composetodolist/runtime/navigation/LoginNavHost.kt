package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.login.ui.LoginScreen
import com.wisnu.kurniawan.composetodolist.features.login.ui.LoginViewModel

fun NavGraphBuilder.LoginNavHost(navController: NavHostController) {
    navigation(startDestination = AuthFlow.LoginScreen.route, route = AuthFlow.Root.route) {
        composable(AuthFlow.LoginScreen.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel)
        }
    }
}
