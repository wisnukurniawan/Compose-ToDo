package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ToDoTaskDb::class,
            parentColumns = ["task_id"],
            childColumns = ["step_taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("step_taskId")
    ]
)
data class ToDoStepDb(
    @PrimaryKey
    @ColumnInfo(name = "step_id")
    val id: String,
    @ColumnInfo(name = "step_name")
    val name: String,
    @ColumnInfo(name = "step_taskId")
    val taskId: String,
    @ColumnInfo(name = "step_status")
    val status: ToDoStatus,
    @ColumnInfo(name = "step_createdAt")
    val createdAt: LocalDateTime,
    @ColumnInfo(name = "step_updatedAt")
    val updatedAt: LocalDateTime,
)
