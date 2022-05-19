package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui.ItemScheduledState
import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus

fun List<TaskWithList>.toItemScheduledState(withHeader: Boolean): List<ItemScheduledState> {
    val items = mutableListOf<ItemScheduledState>()

    if (isEmpty()) return items

    if (withHeader) {
        groupBy { it.task.dueDate?.toLocalDate() }
            .filter { it.key != null }
            .forEach { (key, value) ->
                items.add(ItemScheduledState.Header(key!!))

                value.forEach { (list, task) ->
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
        forEach { (list, task) ->
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
