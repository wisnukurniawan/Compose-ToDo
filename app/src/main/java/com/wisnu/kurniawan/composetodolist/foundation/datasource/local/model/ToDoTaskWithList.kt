package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.Embedded

data class ToDoTaskWithList(
    @Embedded val task: ToDoTaskWithSteps,
    @Embedded(prefix = "list_") val list: ToDoListDb
)
