package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupWithList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListWithTasks
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithSteps
import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

fun List<ToDoTaskDb>.toToDoTask(): List<ToDoTask> {
    return map { it.toToDoTask() }
}

fun List<ToDoTaskWithSteps>.toDoTaskWithStepsToToDoTask(): List<ToDoTask> {
    return map { it.toToDoTask() }
}

fun List<ToDoGroupDb>.toToDoGroup(): List<ToDoGroup> {
    return map { it.toToDoGroup() }
}

fun List<ToDoListDb>.toToDoList(): List<ToDoList> {
    return map { it.toToDoList() }
}

fun List<ToDoListWithTasks>.toDoListWithTasksToToDoList(): List<ToDoList> {
    return map { it.toToDoList() }
}

fun List<ToDoStepDb>.toToDoStep(): List<ToDoStep> {
    return map { step ->
        ToDoStep(
            id = step.id,
            name = step.name,
            status = step.status,
            createdAt = step.createdAt,
            updatedAt = step.updatedAt
        )
    }
}

fun List<ToDoListDb>.toGroupIdWithList(): List<GroupIdWithList> {
    return map { list ->
        GroupIdWithList(
            groupId = list.groupId,
            list = list.toToDoList()
        )
    }
}

fun List<ToDoGroupWithList>.toDoGroupWithListToGroup(): List<ToDoGroup> {
    return map {
        it.group.toToDoGroup(it.listWithTasks.toDoListWithTasksToToDoList())
    }
}

fun ToDoTaskWithSteps.toToDoTask(): ToDoTask {
    return task.toToDoTask(steps.toToDoStep())
}

fun ToDoListWithTasks.toToDoList(): ToDoList {
    return list.toToDoList(taskWithSteps.toDoTaskWithStepsToToDoTask())
}

fun ToDoListDb.toToDoList(tasks: List<ToDoTask> = listOf()): ToDoList {
    return ToDoList(
        id = id,
        name = name,
        color = color,
        tasks = tasks,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ToDoTaskDb.toToDoTask(steps: List<ToDoStep> = listOf()): ToDoTask {
    return ToDoTask(
        id = id,
        name = name,
        status = status,
        steps = steps,
        completedAt = completedAt,
        dueDate = dueDate,
        isDueDateTimeSet = isDueDateTimeSet,
        repeat = repeat,
        note = note,
        noteUpdatedAt = noteUpdatedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ToDoGroupDb.toToDoGroup(lists: List<ToDoList> = listOf()): ToDoGroup {
    return ToDoGroup(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lists = lists
    )
}
