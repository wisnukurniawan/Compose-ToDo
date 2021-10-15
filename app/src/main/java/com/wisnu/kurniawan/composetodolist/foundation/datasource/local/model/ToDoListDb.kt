package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model

import androidx.room.ColumnInfo
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
            parentColumns = ["group_id"],
            childColumns = ["list_groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("list_groupId"),
        Index("list_name", unique = true)
    ]
)
data class ToDoListDb(
    @PrimaryKey
    @ColumnInfo(name = "list_id")
    val id: String,
    @ColumnInfo(name = "list_name")
    val name: String,
    @ColumnInfo(name = "list_color")
    val color: ToDoColor,
    @ColumnInfo(name = "list_groupId")
    val groupId: String,
    @ColumnInfo(name = "list_createdAt")
    val createdAt: LocalDateTime,
    @ColumnInfo(name = "list_updatedAt")
    val updatedAt: LocalDateTime,
)
