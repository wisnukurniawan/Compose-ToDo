package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ToDoListWithTasks(
    @Embedded val list: ToDoListDb,
    @Relation(
        entity = ToDoTaskDb::class,
        parentColumn = "id",
        entityColumn = "listId"
    )
    val taskWithSteps: List<ToDoTaskWithSteps>
)
