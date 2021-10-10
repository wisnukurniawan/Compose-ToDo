package com.wisnu.kurniawan.composetodolist.features.todo.all.ui

import com.wisnu.kurniawan.composetodolist.model.ToDoTask

sealed class AllAction {
    sealed class TaskAction : AllAction() {
        data class Delete(val task: ToDoTask) : TaskAction()
        data class OnToggleStatus(val task: ToDoTask) : TaskAction()
    }
}
