package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ToDoListWithTasks(
    @Embedded val list: ToDoListDb,
    @Relation(
        entity = ToDoTaskDb::class,
        parentColumn = "list_id",
        entityColumn = "task_listId"
    )
    val taskWithSteps: List<ToDoTaskWithSteps>
)
