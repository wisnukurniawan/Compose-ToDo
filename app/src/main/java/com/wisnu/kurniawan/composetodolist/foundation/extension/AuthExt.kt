package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.model.Credential

fun Credential.isLoggedIn() = token.isNotBlank()
