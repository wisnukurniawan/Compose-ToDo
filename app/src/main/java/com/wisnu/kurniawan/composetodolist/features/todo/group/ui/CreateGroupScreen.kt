package com.wisnu.kurniawan.composetodolist.features.todo.group.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.isValidGroupName
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalBackHeader
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalTitle
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgSecondaryButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTextField
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.requestFocusImeAware
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.HandleEffect
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(
    viewModel: CreateGroupViewModel,
    onHideScreen: () -> Unit,
    onShowGroupListScreen: (String) -> Unit,
    onCancelClick: () -> Unit,
) {
    val focusRequest = remember { FocusRequester() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val defaultName = stringResource(R.string.todo_group_default_name)

    HandleEffect(viewModel) {
        when (it) {
            is CreateGroupEffect.HideScreen -> onHideScreen()
            is CreateGroupEffect.ShowUpdateGroupListScreen -> {
                onShowGroupListScreen(it.groupId)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dispatch(CreateGroupAction.InitName(TextFieldValue(defaultName, TextRange(defaultName.length))))
        launch { focusRequest.requestFocusImeAware() }
    }

    CreateGroup(
        modifier = Modifier.focusRequester(focusRequest),
        title = {
            PgModalTitle(
                text = stringResource(R.string.todo_create_group)
            )
        },
        hint = stringResource(R.string.todo_create_group_hint),
        buttonText = stringResource(R.string.todo_create_group_positive),
        groupName = state.groupName,
        isValidGroupName = state.isValidGroupName(),
        onGroupNameChange = { viewModel.dispatch(CreateGroupAction.ChangeGroupName(it)) },
        onImeDoneClick = { viewModel.dispatch(CreateGroupAction.ClickImeDone) },
        onCreateClick = { viewModel.dispatch(CreateGroupAction.ClickSave) },
        onCancelClick = onCancelClick
    )
}

@Composable
fun UpdateGroupScreen(
    viewModel: CreateGroupViewModel,
    onHideScreen: () -> Unit,
    onClickBack: () -> Unit,
    onCancelClick: () -> Unit,
) {
    val focusRequest = remember { FocusRequester() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandleEffect(viewModel) {
        when (it) {
            CreateGroupEffect.HideScreen -> onHideScreen()
            is CreateGroupEffect.ShowUpdateGroupListScreen -> {}
        }
    }

    LaunchedEffect(Unit) {
        launch { focusRequest.requestFocusImeAware() }
    }

    CreateGroup(
        modifier = Modifier.focusRequester(focusRequest),
        title = {
            PgModalBackHeader(
                text = stringResource(R.string.todo_group_menu_rename),
                onClickBack = onClickBack
            )
        },
        hint = stringResource(R.string.todo_group_menu_rename),
        buttonText = stringResource(R.string.todo_rename),
        groupName = state.groupName,
        isValidGroupName = state.isValidGroupName(),
        onGroupNameChange = { viewModel.dispatch(CreateGroupAction.ChangeGroupName(it)) },
        onImeDoneClick = { viewModel.dispatch(CreateGroupAction.ClickImeDone) },
        onCreateClick = { viewModel.dispatch(CreateGroupAction.ClickSave) },
        onCancelClick = onCancelClick
    )
}

@Composable
private fun CreateGroup(
    modifier: Modifier,
    title: @Composable () -> Unit,
    hint: String,
    buttonText: String,
    groupName: TextFieldValue,
    isValidGroupName: Boolean,
    onGroupNameChange: (TextFieldValue) -> Unit,
    onImeDoneClick: () -> Unit,
    onCreateClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    PgModalLayout(
        title = title,
        content = {
            item {
                PgTextField(
                    value = groupName,
                    onValueChange = onGroupNameChange,
                    placeholderValue = hint,
                    modifier = modifier.padding(horizontal = 16.dp).height(50.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onImeDoneClick() }
                    )
                )
            }

            item {
                Row(modifier = Modifier.padding(16.dp)) {
                    PgSecondaryButton(
                        modifier = Modifier.weight(1F),
                        onClick = onCancelClick,
                    ) { Text(text = stringResource(R.string.todo_cancel), color = MaterialTheme.colorScheme.onSecondary) }

                    Spacer(Modifier.width(16.dp))

                    PgButton(
                        modifier = Modifier.weight(1F),
                        onClick = onCreateClick,
                        enabled = isValidGroupName
                    ) { Text(text = buttonText, color = Color.White) }
                }
            }
        }
    )
}
