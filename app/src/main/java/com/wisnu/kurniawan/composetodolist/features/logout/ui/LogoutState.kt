package com.wisnu.kurniawan.composetodolist.features.logout.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.model.User

@Immutable
data class LogoutState(val user: User = User())
