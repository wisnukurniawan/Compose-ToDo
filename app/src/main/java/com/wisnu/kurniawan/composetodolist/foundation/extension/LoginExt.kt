package com.wisnu.kurniawan.composetodolist.foundation.extension

import androidx.core.util.PatternsCompat
import com.wisnu.kurniawan.composetodolist.features.login.ui.LoginState
import com.wisnu.kurniawan.composetodolist.model.Credential

fun LoginState.canLogin() = email.isNotBlank() && password.isNotBlank()

fun LoginState.isValidEmail() = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

fun Credential.isLoggedIn() = token.isNotBlank()
