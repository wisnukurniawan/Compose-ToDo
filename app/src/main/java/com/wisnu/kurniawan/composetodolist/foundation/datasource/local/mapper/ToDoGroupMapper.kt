package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupWithList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList

fun List<ToDoGroupDb>.toGroup(): List<ToDoGroup> {
    return map { it.toGroup() }
}

fun List<ToDoListDb>.toGroupIdWithList(): List<GroupIdWithList> {
    return map { list ->
        GroupIdWithList(
            groupId = list.groupId,
            list = list.toToDoList()
        )
    }
}

fun List<ToDoGroupWithList>.groupWithListToGroup(): List<ToDoGroup> {
    return map {
        it.group.toGroup(it.listWithTasks.toDoListWithTasksToToDoList())
    }
}

fun ToDoGroupDb.toGroup(lists: List<ToDoList> = listOf()): ToDoGroup {
    return ToDoGroup(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lists = lists
    )
}

fun List<ToDoGroup>.toGroupDp(): List<ToDoGroupDb> {
    return map {
        ToDoGroupDb(
            id = it.id,
            name = it.name,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )
    }
}
