package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDate

@Immutable
data class ScheduledState(
    val items: List<ItemScheduledState> = listOf()
)

sealed class ItemScheduledState {
    data class Header(val date: LocalDate) : ItemScheduledState()

    data class Complete(
        val toDoTask: ToDoTask,
        val toDoList: ToDoList
    ) : ItemScheduledState()

    data class InProgress(
        val toDoTask: ToDoTask,
        val toDoList: ToDoList
    ) : ItemScheduledState()
}
