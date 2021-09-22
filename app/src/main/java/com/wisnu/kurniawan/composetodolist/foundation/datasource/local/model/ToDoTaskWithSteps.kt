package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ToDoTaskWithSteps(
    @Embedded val task: ToDoTaskDb,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val steps: List<ToDoStepDb>
)
