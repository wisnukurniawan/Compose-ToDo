package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.R
import com.wisnu.kurniawan.composetodolist.features.todo.step.ui.ToDoRepeatItem
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat

fun List<ToDoRepeatItem>.select(repeat: ToDoRepeat): List<ToDoRepeatItem> {
    return map {
        it.copy(applied = it.repeat == repeat)
    }
}

fun ToDoRepeat.displayable(): Int {
    return when (this) {
        ToDoRepeat.NEVER -> R.string.todo_repeat_never
        ToDoRepeat.DAILY -> R.string.todo_repeat_daily
        ToDoRepeat.WEEKDAYS -> R.string.todo_repeat_weekdays
        ToDoRepeat.WEEKLY -> R.string.todo_repeat_weekly
        ToDoRepeat.MONTHLY -> R.string.todo_repeat_monthly
        ToDoRepeat.YEARLY -> R.string.todo_repeat_yearly
    }
}
