package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.wisnu.kurniawan.composetodolist.features.splash.ui.SplashScreen
import com.wisnu.kurniawan.composetodolist.features.splash.ui.SplashViewModel
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.isTablet
import com.wisnu.kurniawan.composetodolist.runtime.defaultMainBottomSheetConfig

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun MainNavHost() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val bottomSheetConfig = remember { mutableStateOf(defaultMainBottomSheetConfig) }

    val navController = rememberNavController(bottomSheetNavigator)

    val isTablet = isTablet()

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

            SettingNavHost(navController, bottomSheetConfig)

            if (isTablet) {
                composable(HomeFlow.Root.route) {
                    HomeTabletNavHost(navController)
                }
            } else {
                HomeNavHost(navController, bottomSheetConfig)

                ListDetailNavHost(navController, bottomSheetConfig)

                StepNavHost(navController, bottomSheetConfig)
            }
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
private fun HomeTabletNavHost(navController: NavHostController) {
    val bottomSheetNavigatorLeft = rememberBottomSheetNavigator()
    val bottomSheetConfigLeft = remember { mutableStateOf(defaultMainBottomSheetConfig) }
    val navControllerLeft = rememberNavController(bottomSheetNavigatorLeft)

    val bottomSheetNavigatorRight = rememberBottomSheetNavigator()
    val bottomSheetConfigRight = remember { mutableStateOf(defaultMainBottomSheetConfig) }
    val navControllerRight = rememberNavController(bottomSheetNavigatorRight)

    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxHeight().weight(0.4F)) {
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigatorLeft,
                sheetShape = bottomSheetConfigLeft.value.sheetShape,
                scrimColor = if (bottomSheetConfigLeft.value.showScrim) {
                    ModalBottomSheetDefaults.scrimColor
                } else {
                    Color.Transparent
                }
            ) {
                NavHost(
                    navController = navControllerLeft,
                    startDestination = HomeFlow.Root.route
                ) {
                    HomeTabletNavHost(
                        navController,
                        navControllerLeft,
                        navControllerRight,
                        bottomSheetConfigLeft
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        )

        Box(modifier = Modifier.fillMaxHeight().weight(0.6F)) {
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigatorRight,
                sheetShape = bottomSheetConfigRight.value.sheetShape,
                scrimColor = if (bottomSheetConfigRight.value.showScrim) {
                    ModalBottomSheetDefaults.scrimColor
                } else {
                    Color.Transparent
                }
            ) {
                NavHost(
                    navController = navControllerRight,
                    startDestination = ListDetailFlow.RootEmpty.route
                ) {
                    composable(route = ListDetailFlow.RootEmpty.route) {

                    }

                    ListDetailTabletNavHost(navControllerRight, bottomSheetConfigRight)

                    StepNavHost(navControllerRight, bottomSheetConfigRight)
                }
            }
        }
    }
}
