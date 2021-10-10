package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.foundation.extension.toToDoTaskItem
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDate

@Immutable
data class ScheduledState(
    val tasks: List<Pair<ToDoTask, ToDoList>> = listOf()
) {
    val taskDisplayable = tasks.toToDoTaskItem()
}

sealed class ToDoTaskItem {
    data class Header(val date: LocalDate) : ToDoTaskItem()

    data class Complete(
        val toDoTask: ToDoTask,
        val toDoList: ToDoList
    ) : ToDoTaskItem()

    data class InProgress(
        val toDoTask: ToDoTask,
        val toDoList: ToDoList
    ) : ToDoTaskItem()
}
