package com.wisnu.kurniawan.composetodolist.features.todo.all.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

@Immutable
data class AllState(
    val lists: List<ToDoList> = listOf(),
    val hideCompleteTask: Boolean = true,
) {
    val items = lists
        .filterCompleteTask(shouldFilter = hideCompleteTask)
        .toItemAllState()
}

sealed class ItemAllState {
    data class List(val list: ToDoList) : ItemAllState()

    sealed class Task(
        open val task: ToDoTask,
        open val list: ToDoList
    ) : ItemAllState() {
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
