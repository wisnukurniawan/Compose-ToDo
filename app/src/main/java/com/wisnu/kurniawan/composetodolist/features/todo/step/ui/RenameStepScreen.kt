package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoCreateConfirmator
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.requestFocusImeAware
import kotlinx.coroutines.launch

@Composable
fun RenameStepScreen(
    navController: NavController,
    viewModel: StepViewModel,
    stepId: String
) {
    val state by viewModel.state.collectAsState()
    val focusRequest = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        launch { focusRequest.requestFocusImeAware() }
        viewModel.dispatch(StepAction.StepItemAction.Edit.OnShow(stepId))
    }

    PgToDoCreateConfirmator(
        title = stringResource(R.string.todo_step_rename),
        positiveText = stringResource(R.string.todo_rename),
        name = state.editStepName,
        placeholder = stringResource(R.string.todo_step_rename),
        isValidName = state.validEditStepName,
        focusRequester = focusRequest,
        onNameChange = { viewModel.dispatch(StepAction.StepItemAction.Edit.ChangeStepName(it)) },
        onCancelClick = { navController.navigateUp() },
        onSaveClick = {
            viewModel.dispatch(StepAction.StepItemAction.Edit.ClickSave(stepId))
            navController.navigateUp()
        }
    )
}
