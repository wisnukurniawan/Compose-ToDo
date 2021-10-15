package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    indices = [
        Index("group_name", unique = true)
    ]
)
data class ToDoGroupDb(
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    val id: String,
    @ColumnInfo(name = "group_name")
    val name: String,
    @ColumnInfo(name = "group_createdAt")
    val createdAt: LocalDateTime,
    @ColumnInfo(name = "group_updatedAt")
    val updatedAt: LocalDateTime,
) {
    companion object {
        const val DEFAULT_ID = "default"
    }
}
