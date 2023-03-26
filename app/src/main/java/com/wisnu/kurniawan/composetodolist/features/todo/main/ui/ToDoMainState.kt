package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList

@Immutable
data class ToDoMainState(
    val data: List<ToDoGroup> = listOf(),
    val currentDate: String = "0",
    val allTaskCount: String = "0",
    val scheduledTodayTaskCount: String = "0",
    val scheduledTaskCount: String = "0",
    val selectedItemState: SelectedItemState = SelectedItemState.Empty
) {
    val items = data.toItemGroup(selectedItemState)
    val isAllTaskSelected = selectedItemState == SelectedItemState.AllTask
    val isScheduledTodayTaskSelected = selectedItemState == SelectedItemState.ScheduledTodayTask
    val isScheduledTaskSelected = selectedItemState == SelectedItemState.ScheduledTask
}

sealed class SelectedItemState {
    object Empty : SelectedItemState()
    object AllTask : SelectedItemState()
    object ScheduledTodayTask : SelectedItemState()
    object ScheduledTask : SelectedItemState()
    data class List(val listId: String) : SelectedItemState()
}

sealed class ItemMainState {
    data class ItemGroup(
        val group: ToDoGroup
    ) : ItemMainState()

    sealed class ItemListType(
        open val list: ToDoList,
        open val selected: Boolean,
    ) : ItemMainState() {
        data class First(
            override val list: ToDoList,
            override val selected: Boolean,
        ) : ItemListType(list, selected)

        data class Middle(
            override val list: ToDoList,
            override val selected: Boolean,
        ) : ItemListType(list, selected)

        data class Last(
            override val list: ToDoList,
            override val selected: Boolean,
        ) : ItemListType(list, selected)

        data class Single(
            override val list: ToDoList,
            override val selected: Boolean,
        ) : ItemListType(list, selected)
    }
}
