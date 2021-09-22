package com.wisnu.kurniawan.composetodolist.model

import java.time.LocalDateTime

data class ToDoList(
    val id: String = "",
    val name: String = "",
    val color: ToDoColor = ToDoColor.BLUE,
    val tasks: List<ToDoTask> = listOf(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
