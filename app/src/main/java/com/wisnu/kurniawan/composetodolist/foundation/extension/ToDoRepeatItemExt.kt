package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.ToDoRepeatItem
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat

fun List<ToDoRepeatItem>.update(repeat: ToDoRepeat): List<ToDoRepeatItem> {
    return map {
        it.copy(applied = it.repeat == repeat)
    }
}
