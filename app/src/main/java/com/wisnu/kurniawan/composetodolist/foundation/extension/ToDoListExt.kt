package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.ItemAllState
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ToDoListState
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ToDoTaskItem
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

fun ToDoList.toToDoListState(): ToDoListState {
    val tasks = tasks
        .map {
            when (it.status) {
                ToDoStatus.COMPLETE -> ToDoTaskItem.Complete(it)
                ToDoStatus.IN_PROGRESS -> ToDoTaskItem.InProgress(it)
            }
        }
        .sortedBy { it is ToDoTaskItem.Complete }
        .toMutableList()

    val firstCompleteIndex = tasks.indexOfFirst { it is ToDoTaskItem.Complete }

    if (firstCompleteIndex != -1) {
        tasks.add(firstCompleteIndex, ToDoTaskItem.CompleteHeader())
    }

    return ToDoListState(
        id = id,
        name = name,
        color = color,
        tasks = tasks
    )
}

fun List<ToDoList>.toItemAllState(): List<ItemAllState> {
    val data = mutableListOf<ItemAllState>()

    forEach {
        data.add(ItemAllState.List(it))
        data.addAll(it.tasks.toItemListAllState(it))
    }

    return data
}

private fun List<ToDoTask>.toItemListAllState(toDoList: ToDoList): List<ItemAllState> {
    return map {
        when (it.status) {
            ToDoStatus.IN_PROGRESS -> ItemAllState.Task.InProgress(it, toDoList)
            ToDoStatus.COMPLETE -> ItemAllState.Task.Complete(it, toDoList)
        }
    }
}

fun List<ToDoList>.filterCompleteTask(shouldFilter: Boolean): List<ToDoList> {
    return if (shouldFilter) {
        this.map {
            it.copy(tasks = it.tasks.filter { task -> task.status != ToDoStatus.COMPLETE })
        }.filter { it.tasks.isNotEmpty() }
    } else {
        this
    }
}
