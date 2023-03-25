package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithSteps
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

fun List<ToDoTaskDb>.toTask(): List<ToDoTask> {
    return map { it.toTask() }
}

fun List<ToDoTaskWithSteps>.taskWithStepsToTask(): List<ToDoTask> {
    return map { it.toTask() }
}

fun ToDoTaskWithSteps.toTask(): ToDoTask {
    return task.toTask(steps.toStep())
}

fun ToDoTaskDb.toTask(steps: List<ToDoStep> = listOf()): ToDoTask {
    return ToDoTask(
        id = id,
        name = name,
        status = status,
        steps = steps,
        completedAt = completedAt,
        dueDate = dueDate,
        isDueDateTimeSet = isDueDateTimeSet,
        repeat = repeat,
        note = note,
        noteUpdatedAt = noteUpdatedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<ToDoTask>.toTaskDb(listId: String): List<ToDoTaskDb> {
    return map {
        ToDoTaskDb(
            id = it.id,
            name = it.name,
            status = it.status,
            listId = listId,
            dueDate = it.dueDate,
            completedAt = it.completedAt,
            isDueDateTimeSet = it.isDueDateTimeSet,
            repeat = it.repeat,
            note = it.note,
            noteUpdatedAt = it.noteUpdatedAt,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )
    }
}
