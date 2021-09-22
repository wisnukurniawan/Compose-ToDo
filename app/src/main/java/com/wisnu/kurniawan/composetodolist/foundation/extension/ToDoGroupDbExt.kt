package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup

fun List<ToDoGroupDb>.groupDbToGroup(): List<ToDoGroup> {
    return map { group ->
        ToDoGroup(
            id = group.id,
            name = group.name,
            createdAt = group.createdAt,
            updatedAt = group.updatedAt,
        )
    }
}

fun ToDoGroupDb.groupDbToGroup(): ToDoGroup {
    return ToDoGroup(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
