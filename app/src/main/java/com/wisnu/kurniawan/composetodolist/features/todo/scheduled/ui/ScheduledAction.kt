package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import com.wisnu.kurniawan.composetodolist.model.ToDoTask

sealed class ScheduledAction {
    sealed class TaskAction : ScheduledAction() {
        data class Delete(val task: ToDoTask) : TaskAction()
        data class OnToggleStatus(val task: ToDoTask) : TaskAction()
    }
}
