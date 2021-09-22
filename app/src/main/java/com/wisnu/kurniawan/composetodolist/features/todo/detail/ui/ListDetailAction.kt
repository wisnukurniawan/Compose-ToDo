package com.wisnu.kurniawan.composetodolist.features.todo.detail.ui

import androidx.compose.ui.text.input.TextFieldValue
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

sealed class ListDetailAction {
    sealed class ListAction : ListDetailAction() {

        object Create : ListAction()

        object Update : ListAction()

        object CancelUpdate : ListAction()

        data class ApplyColor(val color: ColorItem) : ListAction()

        data class ChangeName(val name: String) : ListAction()

        data class InitName(val name: String) : ListAction()
    }

    sealed class TaskAction : ListDetailAction() {
        object ClickImeDone : TaskAction()
        object ClickSubmit : TaskAction()
        object OnShow : TaskAction()
        data class Delete(val task: ToDoTask) : TaskAction()
        data class OnToggleStatus(val task: ToDoTask) : TaskAction()
        data class ChangeTaskName(val name: TextFieldValue) : TaskAction()
    }
}
