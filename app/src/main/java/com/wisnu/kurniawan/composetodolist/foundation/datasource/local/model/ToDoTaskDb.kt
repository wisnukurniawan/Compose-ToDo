package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

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
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("listId")
    ]
)
data class ToDoTaskDb(
    @PrimaryKey val id: String,
    val name: String,
    val listId: String,
    val status: ToDoStatus,
    val dueDate: LocalDateTime? = null,
    val completedAt: LocalDateTime? = null,
    val isDueDateTimeSet: Boolean = false,
    val repeat: ToDoRepeat = ToDoRepeat.NEVER,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
