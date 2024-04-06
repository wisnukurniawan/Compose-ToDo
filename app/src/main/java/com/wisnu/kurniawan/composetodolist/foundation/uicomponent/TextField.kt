package com.wisnu.kurniawan.composetodolist.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.theme.AlphaDisabled

const val MAX_TEXT_FIELD_CHARACTER = 255

@Composable
fun PgTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderValue: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = MaterialTheme.shapes.small,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface),
    errorLabel: @Composable (() -> Unit)? = null,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    PgTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        placeholderValue = placeholderValue,
        modifier = modifier,
        shape = shape,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textColor = textColor,
        errorLabel = errorLabel,
        textStyle = textStyle
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PgTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholderValue: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = MaterialTheme.shapes.small,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface),
    errorLabel: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.text.length <= MAX_TEXT_FIELD_CHARACTER) onValueChange(it) },
        placeholder = {
            Text(
                text = placeholderValue,
                style = textStyle,
                color = MaterialTheme.colorScheme.onSurface.copy(AlphaDisabled)
            )
        },
        modifier = modifier
            .height(56.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = shape
            ),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        textStyle = textStyle,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            unfocusedTextColor = textColor
        ),
    )

    if (errorLabel != null && isError) {
        errorLabel()
    }

}

@Composable
fun PgBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderValue: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() }
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    PgBasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        decorationBox = decorationBox,
        placeholderValue = placeholderValue
    )
}

@Composable
fun PgBasicTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholderValue: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() }
) {
    Box {
        BasicTextField(
            value = value,
            onValueChange = { if (it.text.length <= MAX_TEXT_FIELD_CHARACTER) onValueChange(it) },
            textStyle = textStyle,
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = decorationBox
        )

        if (value.text.isEmpty()) {
            Text(
                text = placeholderValue,
                style = textStyle,
                color = MaterialTheme.colorScheme.onSurface.copy(AlphaDisabled)
            )
        }
    }
}

@Composable
fun PgToDoCreator(
    value: TextFieldValue,
    isValid: Boolean,
    placeholder: String,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: () -> Unit,
    onNextSubmit: () -> Unit,
) {
    PgModalRow(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PgTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholderValue = placeholder,
            shape = MaterialTheme.shapes.large,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSubmit()
                }
            ),
            modifier = modifier
                .height(50.dp)
                .weight(0.6F),
            trailingIcon = {
                PgIconButton(
                    onClick = {
                        onNextSubmit()
                    },
                    enabled = isValid,
                    color = if (isValid) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier.size(42.dp)
                ) {
                    PgIcon(
                        imageVector = Icons.Rounded.ArrowUpward,
                        tint = if (isValid) {
                            LocalContentColor.current
                        } else {
                            LocalContentColor.current.copy(alpha = AlphaDisabled)
                        }
                    )
                }
            }
        )

    }
}

@Composable
fun PgToDoCreateConfirmator(
    title: String,
    positiveText: String,
    name: TextFieldValue,
    placeholder: String,
    isValidName: Boolean,
    focusRequester: FocusRequester,
    onNameChange: (TextFieldValue) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    PgModalLayout(
        title = {
            PgModalTitle(
                text = title
            )
        },
        content = {
            item {
                PgTextField(
                    value = name,
                    onValueChange = { onNameChange(it) },
                    placeholderValue = placeholder,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(50.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    shape = MaterialTheme.shapes.large,
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
                        onClick = onSaveClick,
                        enabled = isValidName
                    ) { Text(text = positiveText, color = Color.White) }
                }
            }
        }
    )
}
