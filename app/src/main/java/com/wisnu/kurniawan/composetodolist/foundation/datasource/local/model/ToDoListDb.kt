package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ToDoGroupDb::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("groupId"),
        Index("name", unique = true)
    ]
)
data class ToDoListDb(
    @PrimaryKey val id: String,
    val name: String,
    val color: ToDoColor,
    val groupId: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
