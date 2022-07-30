package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.wisnu.kurniawan.composetodolist.features.splash.ui.SplashScreen
import com.wisnu.kurniawan.composetodolist.features.splash.ui.SplashViewModel
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.rememberBottomSheetNavigator
import com.wisnu.kurniawan.composetodolist.foundation.window.WindowState

const val MinLargeScreenWidth = 585

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun MainNavHost(windowState: WindowState) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val bottomSheetConfig = remember { mutableStateOf(DefaultMainBottomSheetConfig) }

    val smallestScreenWidthDp = LocalConfiguration.current.smallestScreenWidthDp

    val isLargeScreen = smallestScreenWidthDp > MinLargeScreenWidth

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = bottomSheetConfig.value.sheetShape,
        scrimColor = if (bottomSheetConfig.value.showScrim) {
            ModalBottomSheetDefaults.scrimColor
        } else {
            Color.Transparent
        }
    ) {
        if (isLargeScreen) {
            LargeScreenNavHost(bottomSheetNavigator, windowState, bottomSheetConfig)
        } else {
            SmallScreenNavHost(bottomSheetNavigator, bottomSheetConfig)
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
private fun LargeScreenNavHost(
    bottomSheetNavigator: BottomSheetNavigator,
    windowState: WindowState,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    val navController = rememberNavController(bottomSheetNavigator)

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

        composable(HomeFlow.Root.route) {
            if (windowState.isDualPortrait()) {
                HomeTabletNavHost(navController, 1F, 1F)
            } else {
                HomeTabletNavHost(navController, 0.333F, 0.666F)
            }
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
private fun SmallScreenNavHost(
    bottomSheetNavigator: BottomSheetNavigator,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    val navController = rememberNavController(bottomSheetNavigator)
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

        HomeNavHost(navController, bottomSheetConfig)

        ListDetailNavHost(navController, bottomSheetConfig, Icons.Rounded.ChevronLeft)

        StepNavHost(navController, bottomSheetConfig)

        ScheduledNavHost(navController, Icons.Rounded.ChevronLeft)

        ScheduledTodayNavHost(navController, Icons.Rounded.ChevronLeft)

        AllNavHost(navController, Icons.Rounded.ChevronLeft)
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
private fun HomeTabletNavHost(
    navController: NavHostController,
    weightLeft: Float,
    weightRight: Float,
) {
    val bottomSheetNavigatorLeft = rememberBottomSheetNavigator()
    val bottomSheetConfigLeft = remember { mutableStateOf(DefaultMainBottomSheetConfig) }
    val navControllerLeft = rememberNavController(bottomSheetNavigatorLeft)

    val bottomSheetNavigatorRight = rememberBottomSheetNavigator()
    val bottomSheetConfigRight = remember { mutableStateOf(DefaultMainBottomSheetConfig) }
    val navControllerRight = rememberNavController(bottomSheetNavigatorRight)

    Row(modifier = Modifier.fillMaxSize()) {
        // Left column
        Box(modifier = Modifier.fillMaxHeight().weight(weightLeft)) {
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

        // Divider
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
        )

        // Right column
        Box(modifier = Modifier.fillMaxHeight().weight(weightRight)) {
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
                    startDestination = MainFlow.RootEmpty.route
                ) {
                    composable(route = MainFlow.RootEmpty.route) {

                    }

                    ListDetailNavHost(navControllerRight, bottomSheetConfigRight, Icons.Rounded.Close)

                    StepNavHost(navControllerRight, bottomSheetConfigRight)

                    ScheduledNavHost(navControllerRight, Icons.Rounded.Close)

                    ScheduledTodayNavHost(navControllerRight, Icons.Rounded.Close)

                    AllNavHost(navControllerRight, Icons.Rounded.Close)

                    SearchNavHost(navControllerRight)
                }
            }
        }
    }
}
