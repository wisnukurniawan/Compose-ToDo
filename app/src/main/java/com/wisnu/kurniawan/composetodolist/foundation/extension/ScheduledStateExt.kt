package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ItemScheduledState
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

fun List<Pair<ToDoTask, ToDoList>>.toItemScheduledState(withHeader: Boolean): List<ItemScheduledState> {
    val items = mutableListOf<ItemScheduledState>()

    if (isEmpty()) return items

    if (withHeader) {
        groupBy { (task, _) -> task.dueDate?.toLocalDate() }
            .filter { it.key != null }
            .forEach { (key, value) ->
                items.add(ItemScheduledState.Header(key!!))

                value.forEach { (task, list) ->
                    when (task.status) {
                        ToDoStatus.IN_PROGRESS -> {
                            items.add(ItemScheduledState.Task.InProgress(task, list))
                        }
                        ToDoStatus.COMPLETE -> {
                            items.add(ItemScheduledState.Task.Complete(task, list))
                        }
                    }
                }
            }
    } else {
        forEach { (task, list) ->
            when (task.status) {
                ToDoStatus.IN_PROGRESS -> {
                    items.add(ItemScheduledState.Task.InProgress(task, list))
                }
                ToDoStatus.COMPLETE -> {
                    items.add(ItemScheduledState.Task.Complete(task, list))
                }
            }
        }
    }

    return items
}

fun ItemScheduledState.identifier() = when (this) {
    is ItemScheduledState.Header -> date.toString()
    is ItemScheduledState.Task.Complete -> task.id
    is ItemScheduledState.Task.InProgress -> task.id
}
