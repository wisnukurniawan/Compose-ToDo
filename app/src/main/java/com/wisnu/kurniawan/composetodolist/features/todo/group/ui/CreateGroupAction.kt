package com.wisnu.kurniawan.composetodolist.features.todo.group.ui

import androidx.compose.ui.text.input.TextFieldValue

sealed class CreateGroupAction {
    data class ChangeGroupName(val name: TextFieldValue) : CreateGroupAction()
    object ClickImeDone : CreateGroupAction()
    object ClickSave : CreateGroupAction()
    data class InitName(val name: TextFieldValue) : CreateGroupAction()
}
