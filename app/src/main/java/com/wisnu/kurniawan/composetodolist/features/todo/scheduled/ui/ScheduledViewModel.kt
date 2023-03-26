package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data.IScheduledEnvironment
import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
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

fun List<TaskWithList>.toItemScheduledState(withHeader: Boolean): List<ItemScheduledState> {
    val items = mutableListOf<ItemScheduledState>()

    if (isEmpty()) return items

    if (withHeader) {
        groupBy { it.task.dueDate?.toLocalDate() }
            .filter { it.key != null }
            .forEach { (key, value) ->
                items.add(ItemScheduledState.Header(key!!))

                value.forEach { (list, task) ->
                    when (task.status) {
                        ToDoStatus.IN_PROGRESS -> {
                            items.add(ItemScheduledState.Task.InProgress(task, list))
                        }
                        ToDoStatus.COMPLETE -> {
                            items.add(ItemScheduledState.Task.Complete(task, list))
                        }
                    }
                }
            }
    } else {
        forEach { (list, task) ->
            when (task.status) {
                ToDoStatus.IN_PROGRESS -> {
                    items.add(ItemScheduledState.Task.InProgress(task, list))
                }
                ToDoStatus.COMPLETE -> {
                    items.add(ItemScheduledState.Task.Complete(task, list))
                }
            }
        }
    }

    return items
}
