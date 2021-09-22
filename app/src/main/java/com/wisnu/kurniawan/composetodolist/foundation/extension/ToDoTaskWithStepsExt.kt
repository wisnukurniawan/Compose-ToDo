package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithSteps
import com.wisnu.kurniawan.composetodolist.model.ToDoTask


fun List<ToDoTaskWithSteps>.toTask(): List<ToDoTask> {
    return map { it.toTask() }
}

fun ToDoTaskWithSteps.toTask(): ToDoTask {
    return ToDoTask(
        id = this.task.id,
        name = this.task.name,
        status = this.task.status,
        dueDate = this.task.dueDate,
        completedAt = this.task.completedAt,
        isDueDateTimeSet = this.task.isDueDateTimeSet,
        repeat = this.task.repeat,
        steps = this.steps.toStep(),
        createdAt = this.task.createdAt,
        updatedAt = this.task.updatedAt
    )
}
