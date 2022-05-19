package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.SelectedItemState
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

fun List<ToDoGroup>.toTasks(): List<ToDoTask> {
    return flatMap { group ->
        group.lists
            .flatMap { it.tasks }
    }
}

fun List<ToDoGroup>.toItemGroup(selectedItemState: SelectedItemState): List<ItemMainState> {
    val data = mutableListOf<ItemMainState>()

    forEach {
        if (it.id != ToDoGroupDb.DEFAULT_ID) {
            data.add(ItemMainState.ItemGroup(it))
        }
        data.addAll(it.lists.toItemListMainState(selectedItemState))
    }

    return data
}

private fun List<ToDoList>.toItemListMainState(selectedItemState: SelectedItemState): List<ItemMainState.ItemListType> {
    return mapIndexed { index, list ->
        val selected = selectedItemState is SelectedItemState.List && selectedItemState.listId == list.id
        if (size == 1) {
            ItemMainState.ItemListType.Single(
                list = list,
                selected = selected
            )
        } else {
            when (index) {
                0 -> ItemMainState.ItemListType.First(
                    list = list,
                    selected = selected
                )
                lastIndex -> ItemMainState.ItemListType.Last(
                    list = list,
                    selected = selected
                )
                else -> ItemMainState.ItemListType.Middle(
                    list = list,
                    selected = selected
                )
            }
        }
    }
}

fun ToDoList.totalTask() = tasks.filter { it.status == ToDoStatus.IN_PROGRESS }.size
