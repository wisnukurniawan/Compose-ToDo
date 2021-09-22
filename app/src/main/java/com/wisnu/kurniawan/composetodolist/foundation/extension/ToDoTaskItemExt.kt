package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.ToDoTaskItem

fun ToDoTaskItem.identifier() = when (this) {
    is ToDoTaskItem.CompleteHeader -> id
    is ToDoTaskItem.Complete -> toDoTask.id
    is ToDoTaskItem.InProgress -> toDoTask.id
}
