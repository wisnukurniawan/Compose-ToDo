package com.wisnu.kurniawan.composetodolist.foundation.extension

import androidx.core.util.PatternsCompat
import com.wisnu.kurniawan.composetodolist.features.login.ui.LoginState

fun LoginState.canLogin() = email.isNotBlank() && password.isNotBlank()

fun LoginState.isValidEmail() = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()




