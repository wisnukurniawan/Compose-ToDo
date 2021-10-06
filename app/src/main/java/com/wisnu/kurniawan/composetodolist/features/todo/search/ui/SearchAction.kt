package com.wisnu.kurniawan.composetodolist.features.todo.search.ui

import androidx.compose.ui.text.input.TextFieldValue

sealed class SearchAction {
    data class ChangeSearchText(val text: TextFieldValue) : SearchAction()
    object OnShow : SearchAction()
}
