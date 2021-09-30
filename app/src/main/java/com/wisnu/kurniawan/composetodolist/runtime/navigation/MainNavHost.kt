package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.wisnu.kurniawan.composetodolist.features.splash.ui.SplashScreen
import com.wisnu.kurniawan.composetodolist.features.splash.ui.SplashViewModel
import com.wisnu.kurniawan.composetodolist.runtime.defaultMainBottomSheetConfig

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun MainNavHost() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val bottomSheetConfig = remember { mutableStateOf(defaultMainBottomSheetConfig) }

    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = bottomSheetConfig.value.sheetShape,
        scrimColor = if (bottomSheetConfig.value.showScrim) {
            ModalBottomSheetDefaults.scrimColor
        } else {
            Color.Transparent
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = MainFlow.Root.route
        ) {
            composable(route = MainFlow.Root.route) {
                val viewModel = hiltViewModel<SplashViewModel>()
                SplashScreen(navController = navController, viewModel = viewModel)
            }

            AuthNavHost(navController)

            HomeNavHost(navController, bottomSheetConfig)

            SettingNavHost(navController, bottomSheetConfig)

            ListDetailNavHost(navController, bottomSheetConfig)

            StepNavHost(navController, bottomSheetConfig)
        }
    }
}
