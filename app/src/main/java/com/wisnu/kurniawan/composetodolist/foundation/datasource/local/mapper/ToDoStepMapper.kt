package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.model.ToDoStep

fun List<ToDoStepDb>.toStep(): List<ToDoStep> {
    return map { step ->
        ToDoStep(
            id = step.id,
            name = step.name,
            status = step.status,
            createdAt = step.createdAt,
            updatedAt = step.updatedAt
        )
    }
}

fun List<ToDoStep>.toStepDb(taskId: String): List<ToDoStepDb> {
    return map {
        ToDoStepDb(
            id = it.id,
            name = it.name,
            status = it.status,
            taskId = taskId,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )
    }
}
