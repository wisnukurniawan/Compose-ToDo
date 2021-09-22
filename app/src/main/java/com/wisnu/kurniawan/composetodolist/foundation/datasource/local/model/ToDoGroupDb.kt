package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = [
        Index("name", unique = true)
    ]
)
data class ToDoGroupDb(
    @PrimaryKey val id: String,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        const val DEFAULT_ID = "default"
    }
}
