package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList

@Immutable
data class ToDoMainState(
    val data: List<ItemMainState> = listOf(),
    val currentDate: String = "0",
    val allTaskCount: String = "0",
    val scheduledTodayTaskCount: String = "0",
    val scheduledTaskCount: String = "0",
)

sealed class ItemMainState {
    data class ItemGroup(
        val group: ToDoGroup
    ) : ItemMainState()

    sealed class ItemListType(
        open val list: ToDoList
    ) : ItemMainState() {
        data class First(
            override val list: ToDoList
        ) : ItemListType(list)

        data class Middle(
            override val list: ToDoList
        ) : ItemListType(list)

        data class Last(
            override val list: ToDoList
        ) : ItemListType(list)

        data class Single(
            override val list: ToDoList
        ) : ItemListType(list)
    }
}
