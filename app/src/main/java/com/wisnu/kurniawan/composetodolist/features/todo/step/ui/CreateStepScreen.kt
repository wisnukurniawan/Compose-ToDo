package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgToDoCreator
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.requestFocusImeAware
import kotlinx.coroutines.launch

@Composable
fun CreateStepScreen(
    viewModel: StepViewModel,
) {
    val state by viewModel.state.collectAsState()
    val focusRequest = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        launch { focusRequest.requestFocusImeAware() }
        viewModel.dispatch(StepAction.StepItemAction.Create.OnShow)
    }

    PgToDoCreator(
        value = state.createStepName,
        modifier = Modifier.focusRequester(focusRequest),
        isValid = state.validCreateStepName,
        placeholder = stringResource(R.string.todo_step_next),
        onValueChange = { viewModel.dispatch(StepAction.StepItemAction.Create.ChangeStepName(it)) },
        onSubmit = { viewModel.dispatch(StepAction.StepItemAction.Create.ClickSubmit) },
        onNextSubmit = { viewModel.dispatch(StepAction.StepItemAction.Create.ClickImeDone) }
    )
}
