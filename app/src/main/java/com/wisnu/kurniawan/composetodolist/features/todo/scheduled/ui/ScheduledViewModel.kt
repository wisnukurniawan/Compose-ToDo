package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data.IScheduledEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduledViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    scheduledEnvironment: IScheduledEnvironment,
) : StatefulViewModel<ScheduledState, Unit, ScheduledAction, IScheduledEnvironment>(ScheduledState(), scheduledEnvironment) {

    init {
        viewModelScope.launch(environment.dispatcher) {
            environment.getToDoTaskWithStepsOrderByDueDateWithList()
                .collect {
                    setState { copy(tasks = it) }
                }
        }
    }

    override fun dispatch(action: ScheduledAction) {
        when (action) {
            is ScheduledAction.TaskAction.Delete -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.deleteTask(action.task)
                }
            }
            is ScheduledAction.TaskAction.OnToggleStatus -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.toggleTaskStatus(action.task)
                }
            }
        }
    }
}
