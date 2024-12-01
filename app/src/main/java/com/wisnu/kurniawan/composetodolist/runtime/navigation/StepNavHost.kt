package com.wisnu.kurniawan.composetodolist.runtime.navigation

import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.CreateStepScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.RenameStepScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.RenameTaskScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.RepeatSelectionScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.StepScreen
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.StepViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.UpdateTaskNoteScreen

fun NavGraphBuilder.StepNavHost(
    navController: NavHostController,
    bottomSheetConfig: MutableState<MainBottomSheetConfig>
) {
    navigation(
        startDestination = StepFlow.TaskDetailScreen.route,
        route = StepFlow.Root.route
    ) {
        composable(
            route = StepFlow.TaskDetailScreen.route,
            arguments = StepFlow.TaskDetailScreen.arguments,
            deepLinks = StepFlow.TaskDetailScreen.deepLinks
        ) {
            val viewModel = hiltViewModel<StepViewModel>()
            StepScreen(
                viewModel = viewModel,
                onClickBack = { navController.navigateUp() },
                onClickTaskName = { navController.navigate(StepFlow.EditTask.route) },
                onClickCreateStep = { navController.navigate(StepFlow.CreateStep.route) },
                onClickStep = { navController.navigate(StepFlow.EditStep.route(it)) },
                onClickTaskDelete = { navController.navigateUp() },
                onClickRepeatItem = { navController.navigate(StepFlow.SelectRepeatTask.route) },
                onClickUpdateNote = { navController.navigate(StepFlow.UpdateTaskNote.route) }
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
            bottomSheetConfig.value = NoScrimSmallShapeMainBottomSheetConfig

            CreateStepScreen(viewModel = viewModel)
        }
        bottomSheet(
            route = StepFlow.EditStep.route,
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
            bottomSheetConfig.value = DefaultMainBottomSheetConfig

            RenameStepScreen(
                viewModel = viewModel,
                stepId = stepId.orEmpty(),
                onCancelClick = { navController.navigateUp() },
                onSaveClick = { navController.navigateUp() }
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
            bottomSheetConfig.value = DefaultMainBottomSheetConfig

            RenameTaskScreen(
                viewModel = viewModel,
                onCancelClick = { navController.navigateUp() },
                onSaveClick = { navController.navigateUp() }
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
            bottomSheetConfig.value = DefaultMainBottomSheetConfig

            RepeatSelectionScreen(
                viewModel = viewModel,
                onItemClick = { navController.navigateUp() }
            )
        }
        bottomSheet(StepFlow.UpdateTaskNote.route) {
            val viewModel = if (navController.previousBackStackEntry != null) {
                hiltViewModel<StepViewModel>(
                    navController.previousBackStackEntry!!
                )
            } else {
                hiltViewModel()
            }
            bottomSheetConfig.value = DefaultMainBottomSheetConfig

            UpdateTaskNoteScreen(
                viewModel = viewModel,
                onClickBack = { navController.navigateUp() }
            )
        }
    }
}
