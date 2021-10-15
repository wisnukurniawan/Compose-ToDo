package com.wisnu.kurniawan.composetodolist.model

data class TaskWithList(
    val list: ToDoList,
    val task: ToDoTask
)
