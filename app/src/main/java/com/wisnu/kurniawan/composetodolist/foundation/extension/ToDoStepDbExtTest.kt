package com.wisnu.kurniawan.composetodolist.foundation.extension

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
