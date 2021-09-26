package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

fun ToDoTaskDb.toTask(): ToDoTask {
    return ToDoTask(
        id = id,
        name = name,
        status = status,
        steps = listOf(),
        completedAt = completedAt,
        dueDate = dueDate,
        isDueDateTimeSet = isDueDateTimeSet,
        repeat = repeat,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<ToDoTaskDb>.toTask(): List<ToDoTask> {
    return map { it.toTask() }
}
