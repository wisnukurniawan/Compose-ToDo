package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

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
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("taskId")
    ]
)
data class ToDoStepDb(
    @PrimaryKey val id: String,
    val name: String,
    val taskId: String,
    val status: ToDoStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
