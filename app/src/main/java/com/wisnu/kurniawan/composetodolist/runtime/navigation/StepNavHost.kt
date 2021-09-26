package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.CreateStepScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.RenameStepScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.RenameTaskScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.RepeatSelectionScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.StepScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.StepViewModel
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.bottomSheet
import com.wisnu.kurniawan.composetodolist.runtime.MainBottomSheetConfig
import com.wisnu.kurniawan.composetodolist.runtime.defaultMainBottomSheetConfig
import com.wisnu.kurniawan.composetodolist.runtime.noScrimSmallShapeMainBottomSheetConfig

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.StepNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    navigation(
        startDestination = StepFlow.TaskDetailScreen.routeRegistry,
        route = StepFlow.Root.routeRegistry
    ) {
        composable(
            route = StepFlow.TaskDetailScreen.routeRegistry,
            arguments = StepFlow.TaskDetailScreen.arguments,
            deepLinks = StepFlow.TaskDetailScreen.deepLinks
        ) {
            val viewModel = hiltViewModel<StepViewModel>()
            StepScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        bottomSheet(StepFlow.CreateStep.route) {
            val viewModel = if (navController.previousBackStackEntry != null) {
                hiltViewModel<StepViewModel>(
                    navController.previousBackStackEntry!!
                )
            } else {
                hiltViewModel()
            }
            bottomSheetConfig.value = noScrimSmallShapeMainBottomSheetConfig

            CreateStepScreen(viewModel = viewModel)
        }
        bottomSheet(
            route = StepFlow.EditStep.routeRegistry,
            arguments = StepFlow.EditStep.arguments
        ) { backStackEntry ->
            val viewModel = if (navController.previousBackStackEntry != null) {
                hiltViewModel<StepViewModel>(
                    navController.previousBackStackEntry!!
                )
            } else {
                hiltViewModel()
            }
            val stepId = backStackEntry.arguments?.getString(ARG_STEP_ID)
            bottomSheetConfig.value = defaultMainBottomSheetConfig

            RenameStepScreen(
                navController = navController,
                viewModel = viewModel,
                stepId = stepId.orEmpty()
            )
        }
        bottomSheet(StepFlow.EditTask.route) {
            val viewModel = if (navController.previousBackStackEntry != null) {
                hiltViewModel<StepViewModel>(
                    navController.previousBackStackEntry!!
                )
            } else {
                hiltViewModel()
            }
            bottomSheetConfig.value = defaultMainBottomSheetConfig

            RenameTaskScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        bottomSheet(StepFlow.SelectRepeatTask.route) {
            val viewModel = if (navController.previousBackStackEntry != null) {
                hiltViewModel<StepViewModel>(
                    navController.previousBackStackEntry!!
                )
            } else {
                hiltViewModel()
            }
            bottomSheetConfig.value = defaultMainBottomSheetConfig

            RepeatSelectionScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
