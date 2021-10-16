package com.wisnu.kurniawan.composetodolist.features.todo.search.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.wisnu.kurniawan.composetodolist.foundation.extension.toItemAllState
import com.wisnu.kurniawan.composetodolist.model.ToDoList

@Immutable
data class SearchState(
    val searchText: TextFieldValue = TextFieldValue(),
    val lists: List<ToDoList> = listOf(),
) {
    val items = lists.toItemAllState()
}
