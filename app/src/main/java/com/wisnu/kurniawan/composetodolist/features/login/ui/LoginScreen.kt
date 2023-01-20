package com.wisnu.kurniawan.composetodolist.features.login.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.foundation.extension.canLogin
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgButton
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgPageLayout
import com.wisnu.kurniawan.composetodolist.foundation.uicomponent.PgTextField
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.HandleEffect

@Composable
fun LoginScreen(viewModel: LoginViewModel, onNavigateToDashboard: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onEmailChanged = { viewModel.dispatch(LoginAction.ChangeEmail(it)) },
        onPasswordChanged = { viewModel.dispatch(LoginAction.ChangePassword(it)) },
        onPasswordImeAction = { viewModel.dispatch(LoginAction.ClickImePasswordDone) },
        onClickLogin = { viewModel.dispatch(LoginAction.ClickLogin) }
    )

    HandleEffect(viewModel) {
        when (it) {
            LoginEffect.NavigateToDashboard -> {
                onNavigateToDashboard()
            }
        }
    }
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordImeAction: () -> Unit,
    onClickLogin: () -> Unit,
) {
    PgPageLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                val passwordFocusRequest = remember { FocusRequester() }

                EmailField(
                    email = state.email,
                    isError = state.showEmailInvalidError,
                    onEmailChanged = onEmailChanged,
                    onImeAction = { passwordFocusRequest.requestFocus() },
                    errorLabel = { TextFieldError(stringResource(R.string.login_email_invalid)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                PasswordField(
                    password = state.password,
                    modifier = Modifier.focusRequester(passwordFocusRequest),
                    onPasswordChanged = onPasswordChanged,
                    onImeAction = onPasswordImeAction
                )

                Spacer(modifier = Modifier.height(16.dp))

                PgButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.canLogin(),
                    onClick = onClickLogin
                ) { Text(text = stringResource(R.string.login_login), color = Color.White) }
            }
        }
    }
}

@Composable
private fun EmailField(
    email: String,
    isError: Boolean,
    onEmailChanged: (String) -> Unit,
    onImeAction: () -> Unit,
    errorLabel: @Composable (() -> Unit)? = null
) {
    PgTextField(
        value = email,
        onValueChange = { onEmailChanged(it) },
        placeholderValue = stringResource(R.string.login_email),
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() }
        ),
        modifier = Modifier.fillMaxWidth()
    )

    if (errorLabel != null && isError) {
        errorLabel()
    }
}

@Composable
private fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.error)
        )
    }
}

@Composable
private fun PasswordField(
    password: String,
    modifier: Modifier = Modifier,
    onPasswordChanged: (String) -> Unit,
    onImeAction: () -> Unit,
) {
    val showPassword = remember { mutableStateOf(false) }

    PgTextField(
        value = password,
        onValueChange = { onPasswordChanged(it) },
        modifier = modifier.fillMaxWidth(),
        placeholderValue = stringResource(id = R.string.login_password),
        trailingIcon = {
            IconButton(onClick = { showPassword.value = !showPassword.value }) {
                Icon(
                    imageVector = if (showPassword.value) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = stringResource(id = R.string.login_password)
                )
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() }
        )
    )
}
