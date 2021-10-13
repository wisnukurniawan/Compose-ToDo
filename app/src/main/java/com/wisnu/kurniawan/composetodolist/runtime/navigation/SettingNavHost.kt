package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.wisnu.kurniawan.composetodolist.features.localized.setting.ui.LanguageScreen
import com.wisnu.kurniawan.composetodolist.features.localized.setting.ui.LocalizedSettingViewModel
import com.wisnu.kurniawan.composetodolist.features.logout.ui.LogoutScreen
import com.wisnu.kurniawan.composetodolist.features.logout.ui.LogoutViewModel
import com.wisnu.kurniawan.composetodolist.features.setting.ui.SettingScreen
import com.wisnu.kurniawan.composetodolist.features.setting.ui.SettingViewModel
import com.wisnu.kurniawan.composetodolist.features.theme.ui.ThemeScreen
import com.wisnu.kurniawan.composetodolist.features.theme.ui.ThemeViewModel

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.SettingNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    navigation(startDestination = SettingFlow.Setting.route, route = SettingFlow.Root.route) {
        bottomSheet(SettingFlow.Setting.route) {
            val viewModel = hiltViewModel<SettingViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            SettingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        bottomSheet(SettingFlow.Theme.route) {
            val viewModel = hiltViewModel<ThemeViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            ThemeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        bottomSheet(SettingFlow.Logout.route) {
            val viewModel = hiltViewModel<LogoutViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            LogoutScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(SettingFlow.Language.route) {
            val viewModel = hiltViewModel<LocalizedSettingViewModel>()
            bottomSheetConfig.value = DefaultMainBottomSheetConfig
            LanguageScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
