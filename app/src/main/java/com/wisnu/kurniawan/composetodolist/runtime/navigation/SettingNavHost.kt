package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.localized.setting.ui.LanguageScreen
import com.wisnu.kurniawan.composetodolist.features.localized.setting.ui.LocalizedSettingViewModel
import com.wisnu.kurniawan.composetodolist.features.logout.ui.LogoutScreen
import com.wisnu.kurniawan.composetodolist.features.logout.ui.LogoutViewModel
import com.wisnu.kurniawan.composetodolist.features.setting.ui.SettingScreen
import com.wisnu.kurniawan.composetodolist.features.setting.ui.SettingViewModel
import com.wisnu.kurniawan.composetodolist.features.theme.ui.ThemeScreen
import com.wisnu.kurniawan.composetodolist.features.theme.ui.ThemeViewModel

fun NavGraphBuilder.SettingNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    navigation(startDestination = SettingFlow.Setting.route, route = SettingFlow.Root.route) {
        bottomSheet(SettingFlow.Setting.route) {
            val viewModel = hiltViewModel<SettingViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            SettingScreen(
                viewModel = viewModel,
                onClickLogout = { navController.navigate(SettingFlow.Logout.route) },
                onClickLanguage = { navController.navigate(SettingFlow.Language.route) },
                onClickTheme = { navController.navigate(SettingFlow.Theme.route) }
            )
        }
        bottomSheet(SettingFlow.Theme.route) {
            val viewModel = hiltViewModel<ThemeViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            ThemeScreen(
                viewModel = viewModel,
                onClickBack = { navController.navigateUp() }
            )
        }
        bottomSheet(SettingFlow.Logout.route) {
            val viewModel = hiltViewModel<LogoutViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            LogoutScreen(
                viewModel = viewModel,
                onClickBack = { navController.navigateUp() },
                onNavigateToSplash = {
                    navController.navigate(MainFlow.Root.route) {
                        popUpTo(HomeFlow.DashboardScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        bottomSheet(SettingFlow.Language.route) {
            val viewModel = hiltViewModel<LocalizedSettingViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            LanguageScreen(
                viewModel = viewModel,
                onClickBack = { navController.navigateUp() }
            )
        }
    }
}
