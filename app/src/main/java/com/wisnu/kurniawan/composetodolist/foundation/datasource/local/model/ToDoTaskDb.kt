package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ToDoListDb::class,
            parentColumns = ["list_id"],
            childColumns = ["task_listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("task_listId")
    ]
)
data class ToDoTaskDb(
    @PrimaryKey
    @ColumnInfo(name = "task_id")
    val id: String,
    @ColumnInfo(name = "task_name")
    val name: String,
    @ColumnInfo(name = "task_listId")
    val listId: String,
    @ColumnInfo(name = "task_status")
    val status: ToDoStatus,
    @ColumnInfo(name = "task_dueDate")
    val dueDate: LocalDateTime? = null,
    @ColumnInfo(name = "task_completedAt")
    val completedAt: LocalDateTime? = null,
    @ColumnInfo(name = "task_isDueDateTimeSet")
    val isDueDateTimeSet: Boolean = false,
    @ColumnInfo(name = "task_repeat")
    val repeat: ToDoRepeat = ToDoRepeat.NEVER,
    @ColumnInfo(name = "task_note", defaultValue = "")
    val note: String = "",
    @ColumnInfo(name = "task_noteUpdatedAt")
    val noteUpdatedAt: LocalDateTime? = null,
    @ColumnInfo(name = "task_createdAt")
    val createdAt: LocalDateTime,
    @ColumnInfo(name = "task_updatedAt")
    val updatedAt: LocalDateTime,
)
