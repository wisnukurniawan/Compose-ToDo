package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgBasicTextField
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIcon
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgIconButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.SwipeDismiss
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProviderImpl
import com.wisnu.foundation.coreloggr.Loggr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class ToDoStepItemExp(
    val id: String,
    val value: TextFieldValue
)

private data class ToDoStepCreatorExp(
    val editMode: Boolean,
    val value: TextFieldValue
)


@Composable
private fun TaskCell(
    name: TextFieldValue,
    color: Color,
    leftIcon: ImageVector,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onStatusClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = 8.dp)
        ) {
            PgIconButton(
                onClick = onStatusClick,
                color = Color.Transparent,
                modifier = Modifier.size(56.dp)
            ) {
                PgIcon(
                    imageVector = leftIcon,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }

            PgBasicTextField(
                value = name,
                onValueChange = { onValueChange(it) },
                placeholderValue = stringResource(R.string.todo_rename_task),
                modifier = Modifier
                    .onFocusChanged(onFocusChanged),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                textStyle = MaterialTheme.typography.headlineSmall
            )
        }

        Divider(modifier = Modifier.padding(start = 56.dp))
    }
}

@Composable
private fun StepCell(
    name: TextFieldValue,
    modifier: Modifier = Modifier,
    color: Color,
    leftIcon: ImageVector,
    onValueChange: (TextFieldValue) -> Unit,
    onStatusClick: () -> Unit,
    onClickImeDone: () -> Unit,
    onSwipeToDelete: () -> Unit,
) {
    SwipeDismiss(
        modifier = modifier,
        backgroundModifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant),
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column {
                    StepCell(
                        name = name,
                        placeholderValue = stringResource(R.string.todo_step_rename),
                        color = color,
                        leftIcon = leftIcon,
                        onValueChange = onValueChange,
                        onStatusClick = onStatusClick,
                        onClickImeDone = onClickImeDone
                    )

                    Divider(modifier = Modifier.padding(start = 56.dp))
                }
            }
        },
        onDismiss = { onSwipeToDelete() }
    )
}

@Composable
private fun StepCellCreator(
    color: Color,
    onEditModeClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
    ) {
        PgIconButton(
            onClick = onEditModeClick,
            color = Color.Transparent
        ) {
            PgIcon(
                imageVector = Icons.Rounded.Add,
                tint = color
            )
        }

        Text(
            text = stringResource(R.string.todo_step_next),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth().clickable { onEditModeClick() }
        )
    }
}

@Composable
private fun StepCellEditor(
    value: ToDoStepCreatorExp,
    modifier: Modifier,
    color: Color,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState, TextFieldValue) -> Unit,
    onClickImeDone: (TextFieldValue) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var skipFirstFocus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    StepCell(
        name = value.value,
        placeholderValue = stringResource(R.string.todo_step_next),
        modifier = modifier
            .focusRequester(focusRequester),
        fieldModifier = Modifier
            .onFocusChanged {
                if (skipFirstFocus) {
                    onFocusChanged(it, value.value)
                }
                skipFirstFocus = true
            },
        color = color,
        leftIcon = Icons.Rounded.RadioButtonUnchecked,
        onValueChange = onValueChange,
        leftIconEnabled = false,
        onClickImeDone = {
            onClickImeDone(value.value)
        }
    )
}

@Composable
private fun StepCell(
    name: TextFieldValue,
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    placeholderValue: String,
    color: Color,
    leftIcon: ImageVector,
    leftIconEnabled: Boolean = true,
    onValueChange: (TextFieldValue) -> Unit,
    onStatusClick: () -> Unit = {},
    onClickImeDone: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        PgIconButton(
            onClick = onStatusClick,
            color = Color.Transparent,
            enabled = leftIconEnabled
        ) {
            PgIcon(
                imageVector = leftIcon,
                tint = color
            )
        }

        PgBasicTextField(
            value = name,
            onValueChange = {
                if (name.text != it.text) {
                    onValueChange(it)
                }
            },
            placeholderValue = placeholderValue,
            modifier = fieldModifier,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onClickImeDone() }
            )
        )
    }
}

@Composable
private fun DueDateCell() {

}

// Exp https://github.com/google/accompanist/issues/210
@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun TaskStepPreview() {
    // Action
    // Ime done
    // Focus change
    // Delete
    // back pres
    val focusManager = LocalFocusManager.current

    var data by remember { mutableStateOf(listOf<ToDoStepItemExp>()) }

    var creator by remember { mutableStateOf(ToDoStepCreatorExp(editMode = false, value = TextFieldValue())) }
    val scope = rememberCoroutineScope()
    val relocationRequester = remember { BringIntoViewRequester() }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        data.forEach { item ->
            StepCell(
                name = item.value,
                color = ListRed,
                leftIcon = Icons.Rounded.RadioButtonUnchecked,
                onValueChange = { value ->
                    data = data.map {
                        if (it.id == item.id) {
                            item.copy(value = value)
                        } else {
                            it
                        }
                    }
                },
                onStatusClick = {
                    //focusManager.clearFocus()
                },
                onClickImeDone = {
                    //focusManager.clearFocus()
                },
                onSwipeToDelete = {
                    //focusManager.clearFocus()
                },
            )
        }

        if (creator.editMode) {
            StepCellEditor(
                value = creator,
                color = ListRed,
                modifier = Modifier.bringIntoViewRequester(relocationRequester),
                onValueChange = { value ->
                    creator = creator.copy(value = value)

                    scope.launch {
                        relocationRequester.bringIntoView()
                    }
                },
                onFocusChanged = { focusState, value ->
                    Loggr.debug { "wsnkrn ${focusState.isFocused}" }
                    if (!focusState.isFocused) {
                        if (value.text.isNotBlank()) {
                            val newData = data.toMutableList()
                            newData.add(ToDoStepItemExp(IdProviderImpl().generate(), value))
                            data = newData
                        } else {
                            focusManager.clearFocus()
                        }

                        creator = ToDoStepCreatorExp(editMode = false, value = TextFieldValue())
                    }
                },
                onClickImeDone = { value ->
                    if (value.text.isNotBlank()) {
                        val newData = data.toMutableList()
                        newData.add(ToDoStepItemExp(IdProviderImpl().generate(), value))
                        data = newData
                        creator = creator.copy(value = TextFieldValue())

                        scope.launch {
                            delay(50)
                            relocationRequester.bringIntoView()
                        }
                    } else {
                        focusManager.clearFocus()
                        creator = ToDoStepCreatorExp(editMode = false, value = TextFieldValue())
                    }
                }
            )
        } else {
            StepCellCreator(
                color = ListRed,
                onEditModeClick = {
                    // show editor
                    // replace creator with editor
                    creator = ToDoStepCreatorExp(editMode = true, value = TextFieldValue())
                }
            )
        }
    }
}

@Preview
@Composable
private fun TaskCellPreview() {
    var final by remember { mutableStateOf(TextFieldValue("Final")) }
    var modified by remember { mutableStateOf(TextFieldValue()) }
    var temp by remember { mutableStateOf(TextFieldValue()) }
    // when fokus
    //  - set field use modified
    // when not fokus
    //  - if modified not empty -> set final use modified value
    //  - if modified empty -> dont set final
    //  - last step set field use final
    // action done clear focus

    Column(modifier = Modifier.background(Color.White).fillMaxSize()) {
        TaskCell(
            name = temp,
            color = ListRed,
            leftIcon = Icons.Rounded.RadioButtonUnchecked,
            onValueChange = {
                temp = it
            },
            onStatusClick = {},
            onFocusChanged = {
                if (it.isFocused) {
                    temp = modified
                } else {
                    modified = temp
                    if (temp.text.isNotBlank()) {
                        // update to db the temp
                        final = temp
                    }

                    temp = final
                }
            }
        )
        TaskCell(
            name = temp,
            color = ListRed,
            leftIcon = Icons.Rounded.RadioButtonUnchecked,
            onValueChange = {

            },
            onFocusChanged = {},
            onStatusClick = {}
        )
        StepCell(
            name = temp,
            color = ListRed,
            leftIcon = Icons.Rounded.RadioButtonUnchecked,
            onValueChange = {

            },
            onStatusClick = {},
            onClickImeDone = {},
            onSwipeToDelete = {},
        )
    }
}
