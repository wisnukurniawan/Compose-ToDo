package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data.IScheduledEnvironment
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_SCHEDULED_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ScheduledViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    scheduledEnvironment: IScheduledEnvironment,
) : StatefulViewModel<ScheduledState, Unit, ScheduledAction, IScheduledEnvironment>(ScheduledState(), scheduledEnvironment) {

    private val scheduledType = savedStateHandle.get<String>(ARG_SCHEDULED_TYPE)
    private val isScheduled = scheduledType == ScheduledType.SCHEDULED

    init {
        viewModelScope.launch {
            val maxDate = if (isScheduled) {
                null
            } else {
                LocalDateTime.of(environment.dateTimeProvider.now().toLocalDate().plusDays(1), LocalTime.MIN)
            }

            environment.getToDoTaskWithStepsOrderByDueDateWithList(maxDate)
                .collect {
                    setState { copy(tasks = it, isScheduled = isScheduled) }
                }
        }
    }

    override fun dispatch(action: ScheduledAction) {
        when (action) {
            is ScheduledAction.TaskAction.Delete -> {
                viewModelScope.launch {
                    environment.deleteTask(action.task)
                }
            }
            is ScheduledAction.TaskAction.OnToggleStatus -> {
                viewModelScope.launch {
                    environment.toggleTaskStatus(action.task)
                }
            }
            ScheduledAction.ToggleCompleteTaskVisibility -> {
                viewModelScope.launch {
                    val hideCompleteTask = !state.value.hideCompleteTask
                    setState { copy(hideCompleteTask = hideCompleteTask) }
                }
            }
        }
    }
}
