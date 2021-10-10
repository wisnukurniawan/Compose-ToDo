package com.wisnu.kurniawan.composetodolist.features.todo.all.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

@Immutable
data class AllState(val items: List<ItemAllState> = listOf())

sealed class ItemAllState {
    data class List(val list: ToDoList) : ItemAllState()

    data class Complete(
        val task: ToDoTask,
        val list: ToDoList
    ) : ItemAllState()

    data class InProgress(
        val task: ToDoTask,
        val list: ToDoList
    ) : ItemAllState()
}
