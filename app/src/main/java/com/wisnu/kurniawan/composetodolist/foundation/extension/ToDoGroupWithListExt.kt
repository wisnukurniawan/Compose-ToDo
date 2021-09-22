package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup

fun List<ToDoGroupWithList>.toDoGroupWithListToGroup(): List<ToDoGroup> {
    return map { group ->
        ToDoGroup(
            id = group.group.id,
            name = group.group.name,
            lists = group.listWithTasks.toDoListWithTasksToList(),
            createdAt = group.group.createdAt,
            updatedAt = group.group.updatedAt
        )
    }
}
