package com.wisnu.kurniawan.composetodolist.features.todo.group.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.isValidGroupName
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalBackHeader
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgModalTitle
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgSecondaryButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTextField
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.collectAsEffect
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.requestFocusImeAware
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_GROUP_ID
import com.wisnu.kurniawan.composetodolist.runtime.navigation.HomeFlow
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(
    navController: NavController,
    viewModel: CreateGroupViewModel
) {
    val focusRequest = remember { FocusRequester() }
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsEffect()
    val defaultName = stringResource(R.string.todo_group_default_name)

    when (effect) {
        is CreateGroupEffect.HideScreen -> navController.navigateUp()
        is CreateGroupEffect.ShowUpdateGroupListScreen -> {
            LaunchedEffect(effect) {
                navController.navigateUp()
                navController.navigate(HomeFlow.UpdateGroupList.route + "?$ARG_GROUP_ID=${(effect as CreateGroupEffect.ShowUpdateGroupListScreen).groupId}")
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
        onCancelClick = { navController.navigateUp() }
    )
}

@Composable
fun UpdateGroupScreen(
    navController: NavController,
    viewModel: CreateGroupViewModel
) {
    val focusRequest = remember { FocusRequester() }
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsEffect()

    if (effect is CreateGroupEffect.HideScreen) {
        navController.navigateUp()
    }

    LaunchedEffect(Unit) {
        launch { focusRequest.requestFocusImeAware() }
    }

    CreateGroup(
        modifier = Modifier.focusRequester(focusRequest),
        title = {
            PgModalBackHeader(
                text = stringResource(R.string.todo_group_menu_rename),
                onClickBack = { navController.navigateUp() }
            )
        },
        hint = stringResource(R.string.todo_group_menu_rename),
        buttonText = stringResource(R.string.todo_rename),
        groupName = state.groupName,
        isValidGroupName = state.isValidGroupName(),
        onGroupNameChange = { viewModel.dispatch(CreateGroupAction.ChangeGroupName(it)) },
        onImeDoneClick = { viewModel.dispatch(CreateGroupAction.ClickImeDone) },
        onCreateClick = { viewModel.dispatch(CreateGroupAction.ClickSave) },
        onCancelClick = { navController.navigateUp() }
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
                    modifier = modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
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
                    ) { Text(text = stringResource(R.string.todo_cancel), color = MaterialTheme.colors.onSecondary) }

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
