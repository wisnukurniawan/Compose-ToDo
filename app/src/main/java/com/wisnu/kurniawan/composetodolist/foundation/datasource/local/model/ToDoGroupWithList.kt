package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ToDoGroupWithList(
    @Embedded val group: ToDoGroupDb,
    @Relation(
        entity = ToDoListDb::class,
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val listWithTasks: List<ToDoListWithTasks>
)
