package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoStep

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

fun ToDoStep.newStatus(): ToDoStatus {
    return when (status) {
        ToDoStatus.IN_PROGRESS -> ToDoStatus.COMPLETE
        ToDoStatus.COMPLETE -> ToDoStatus.IN_PROGRESS
    }
}
