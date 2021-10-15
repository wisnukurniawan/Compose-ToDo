package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.foundation.extension.toItemScheduledState
import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDate

@Immutable
data class ScheduledState(
    val tasks: List<TaskWithList> = listOf(),
    val hideCompleteTask: Boolean = true,
    val isScheduled: Boolean = true
) {
    private val tasksFiltered = if (hideCompleteTask && isScheduled) {
        tasks.filter { (_, task) ->
            task.status != ToDoStatus.COMPLETE
        }
    } else {
        tasks
    }

    val items: List<ItemScheduledState> = tasksFiltered.toItemScheduledState(isScheduled)
}

sealed class ItemScheduledState {
    data class Header(val date: LocalDate) : ItemScheduledState()

    sealed class Task(
        open val task: ToDoTask,
        open val list: ToDoList
    ) : ItemScheduledState() {
        data class Complete(
            override val task: ToDoTask,
            override val list: ToDoList
        ) : Task(task, list)

        data class InProgress(
            override val task: ToDoTask,
            override val list: ToDoList
        ) : Task(task, list)
    }
}
