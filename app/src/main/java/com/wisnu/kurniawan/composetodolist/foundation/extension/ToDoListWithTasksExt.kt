package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListWithTasks
import com.wisnu.kurniawan.composetodolist.model.ToDoList

fun List<ToDoListWithTasks>.toDoListWithTasksToList(): List<ToDoList> {
    return map { list ->
        ToDoList(
            id = list.list.id,
            name = list.list.name,
            tasks = list.taskWithSteps.toTask(),
            color = list.list.color,
            createdAt = list.list.createdAt,
            updatedAt = list.list.updatedAt
        )
    }
}

fun ToDoListWithTasks.toDoListWithTasksToList(): ToDoList {
    return ToDoList(
        id = list.id,
        name = list.name,
        color = list.color,
        tasks = taskWithSteps.toTask(),
        createdAt = list.createdAt,
        updatedAt = list.updatedAt
    )
}
