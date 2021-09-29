package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import java.time.LocalDateTime


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

fun List<ToDoGroup>.toTasks(): List<ToDoTask> {
    return flatMap { group ->
        group.lists
            .flatMap { it.tasks }
    }
}

fun List<ToDoGroup>.toScheduledTasks(): List<ToDoTask> {
    return toTasks()
        .filter { it.dueDate != null }
}

fun List<ToDoGroup>.toScheduledTodayTasks(currentDateTime: LocalDateTime): List<ToDoTask> {
    return toTasks()
        .filter {
            it.dueDate != null &&
                it.dueDate.toLocalDate() ==
                currentDateTime.toLocalDate()
        }
}

fun List<ToDoGroup>.toItemGroup(): List<ItemMainState> {
    val data = mutableListOf<ItemMainState>()

    forEach {
        if (it.id != ToDoGroupDb.DEFAULT_ID) {
            data.add(ItemMainState.ItemGroup(it))
        }
        data.addAll(it.lists.toItemListMainState())
    }

    return data
}

private fun List<ToDoList>.toItemListMainState(
) = mapIndexed { index, list ->
    if (size == 1) {
        ItemMainState.ItemListType.Single(
            list = list
        )
    } else {
        when (index) {
            0 -> ItemMainState.ItemListType.First(
                list = list
            )
            lastIndex -> ItemMainState.ItemListType.Last(
                list = list
            )
            else -> ItemMainState.ItemListType.Middle(
                list = list
            )
        }
    }
}

fun ToDoList.totalTask() = tasks.filter { it.status == ToDoStatus.IN_PROGRESS }.size
