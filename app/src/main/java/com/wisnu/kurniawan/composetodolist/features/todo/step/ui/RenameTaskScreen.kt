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
fun RenameTaskScreen(
    navController: NavController,
    viewModel: StepViewModel,
) {
    val state by viewModel.state.collectAsState()
    val focusRequest = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        launch { focusRequest.requestFocusImeAware() }
        viewModel.dispatch(StepAction.TaskAction.OnShow)
    }

    PgToDoCreateConfirmator(
        title = stringResource(R.string.todo_rename_task),
        positiveText = stringResource(R.string.todo_rename),
        name = state.editTaskName,
        placeholder = stringResource(R.string.todo_rename_task),
        isValidName = state.validEditTaskName,
        focusRequester = focusRequest,
        onNameChange = { viewModel.dispatch(StepAction.TaskAction.ChangeTaskName(it)) },
        onCancelClick = { navController.navigateUp() },
        onSaveClick = {
            viewModel.dispatch(StepAction.TaskAction.ClickSave)
            navController.navigateUp()
        }
    )
}
