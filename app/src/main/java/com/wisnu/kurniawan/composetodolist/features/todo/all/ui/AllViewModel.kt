package com.wisnu.kurniawan.composetodolist.features.todo.all.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.all.data.IAllEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllViewModel @Inject constructor(
    allEnvironment: IAllEnvironment
) : StatefulViewModel<AllState, Unit, AllAction, IAllEnvironment>(AllState(), allEnvironment) {

    init {
        viewModelScope.launch(environment.dispatcher) {
            environment.getList()
                .collect {
                    setState { copy(lists = it) }
                }
        }
    }

    override fun dispatch(action: AllAction) {
        when (action) {
            is AllAction.TaskAction.Delete -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.deleteTask(action.task)
                }
            }
            is AllAction.TaskAction.OnToggleStatus -> {
                viewModelScope.launch(environment.dispatcher) {
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
