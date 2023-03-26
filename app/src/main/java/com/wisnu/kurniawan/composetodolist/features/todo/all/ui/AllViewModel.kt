package com.wisnu.kurniawan.composetodolist.features.todo.all.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.all.data.IAllEnvironment
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllViewModel @Inject constructor(
    allEnvironment: IAllEnvironment
) : StatefulViewModel<AllState, Unit, AllAction, IAllEnvironment>(AllState(), allEnvironment) {

    init {
        viewModelScope.launch {
            environment.getList()
                .collect {
                    setState { copy(lists = it) }
                }
        }
    }

    override fun dispatch(action: AllAction) {
        when (action) {
            is AllAction.TaskAction.Delete -> {
                viewModelScope.launch {
                    environment.deleteTask(action.task)
                }
            }
            is AllAction.TaskAction.OnToggleStatus -> {
                viewModelScope.launch {
                    environment.toggleTaskStatus(action.task)
                }
            }
            AllAction.ToggleCompleteTaskVisibility -> {
                viewModelScope.launch {
                    val hideCompleteTask = !state.value.hideCompleteTask
                    setState { copy(hideCompleteTask = hideCompleteTask) }
                }
            }
        }
    }

}

fun List<ToDoList>.toItemAllState(): List<ItemAllState> {
    val data = mutableListOf<ItemAllState>()

    forEach {
        data.add(ItemAllState.List(it))
        data.addAll(it.tasks.toItemListAllState(it))
    }

    return data
}

private fun List<ToDoTask>.toItemListAllState(toDoList: ToDoList): List<ItemAllState> {
    return map {
        when (it.status) {
            ToDoStatus.IN_PROGRESS -> ItemAllState.Task.InProgress(it, toDoList)
            ToDoStatus.COMPLETE -> ItemAllState.Task.Complete(it, toDoList)
        }
    }
}

fun List<ToDoList>.filterCompleteTask(shouldFilter: Boolean): List<ToDoList> {
    return if (shouldFilter) {
        this.map {
            it.copy(tasks = it.tasks.filter { task -> task.status != ToDoStatus.COMPLETE })
        }.filter { it.tasks.isNotEmpty() }
    } else {
        this
    }
}

