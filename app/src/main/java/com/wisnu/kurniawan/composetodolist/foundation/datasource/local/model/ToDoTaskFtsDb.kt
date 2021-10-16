package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = ToDoTaskDb::class)
@Entity
data class ToDoTaskFtsDb(
    @ColumnInfo(name = "task_name")
    val name: String,
)
