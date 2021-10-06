package com.wisnu.kurniawan.composetodolist.features.todo.search.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue

@Immutable
data class SearchState(
    val searchText: TextFieldValue = TextFieldValue()
)
