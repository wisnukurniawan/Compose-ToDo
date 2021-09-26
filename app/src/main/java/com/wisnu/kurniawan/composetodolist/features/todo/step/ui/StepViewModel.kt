package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.step.data.IStepEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.extension.defaultTaskLocalTime
import com.wisnu.kurniawan.composetodolist.foundation.extension.update
import com.wisnu.kurniawan.composetodolist.foundation.extension.updatedDate
import com.wisnu.kurniawan.composetodolist.foundation.extension.updatedTime
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_LIST_ID
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_TASK_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stepEnvironment: IStepEnvironment
) :
    StatefulViewModel<StepState, StepEffect, StepAction, IStepEnvironment>(StepState(), stepEnvironment) {

    private val taskId = savedStateHandle.get<String>(ARG_TASK_ID)
    private val listId = savedStateHandle.get<String>(ARG_LIST_ID)

    init {
        initTask()
    }

    private fun initTask() {
        viewModelScope.launch(environment.dispatcher) {
            if (taskId != null && listId != null) {
                environment.getTask(taskId, listId)
                    .collect { (task, color) ->
                        setState { copy(task = task, color = color, repeatItems = repeatItems.update(task.repeat)) }
                    }
            }
        }
    }

    override fun dispatch(action: StepAction) {
        when (action) {
            is StepAction.TaskAction -> handleTaskAction(action)
            is StepAction.StepItemAction -> handleStepItemAction(action)
        }
    }

    private fun handleTaskAction(action: StepAction.TaskAction) {
        when (action) {
            is StepAction.TaskAction.ChangeTaskName -> {
                viewModelScope.launch {
                    setState { copy(editTaskName = action.name) }
                }
            }
            is StepAction.TaskAction.ClickSave -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.updateTask(state.value.editTaskName.text.trim(), state.value.task.id)
                    setState { copy(editTaskName = TextFieldValue()) }
                }
            }
            is StepAction.TaskAction.OnShow -> {
                viewModelScope.launch {
                    setState { copy(editTaskName = editTaskName.copy(text = task.name, selection = TextRange(task.name.length))) }
                }
            }
            is StepAction.TaskAction.OnToggleStatus -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.toggleTaskStatus(state.value.task)
                }
            }
            StepAction.TaskAction.Delete -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.deleteTask(state.value.task)
                }
            }
            is StepAction.TaskAction.SelectRepeat -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.setRepeatTask(state.value.task, action.repeatItem.repeat)
                }
            }
            StepAction.TaskAction.ResetDueDate -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.resetTaskDueDate(state.value.task.id)
                }
            }
            StepAction.TaskAction.ResetTime -> {
                viewModelScope.launch(environment.dispatcher) {
                    val newDateTime = state.value.task.updatedTime(environment.dateTimeProvider.now().toLocalDate(), defaultTaskLocalTime())
                    environment.resetTaskTime(newDateTime, state.value.task.id)
                }
            }
            is StepAction.TaskAction.SelectDueDate -> {
                viewModelScope.launch(environment.dispatcher) {
                    val newDateTime = state.value.task.updatedDate(action.date)
                    environment.updateTaskDueDate(newDateTime, state.value.task.isDueDateTimeSet, state.value.task.id)
                }
            }
            is StepAction.TaskAction.SelectTime -> {
                viewModelScope.launch(environment.dispatcher) {
                    val newDateTime = state.value.task.updatedTime(environment.dateTimeProvider.now().toLocalDate(), action.time)
                    environment.updateTaskDueDate(newDateTime, true, state.value.task.id)
                }
            }
        }
    }

    private fun handleStepItemAction(action: StepAction.StepItemAction) {
        when (action) {
            is StepAction.StepItemAction.Create -> handleStepItemCreateAction(action)
            is StepAction.StepItemAction.Edit -> handleStepItemEditAction(action)
        }
    }

    private fun handleStepItemCreateAction(action: StepAction.StepItemAction.Create) {
        when (action) {
            is StepAction.StepItemAction.Create.ChangeStepName -> {
                viewModelScope.launch {
                    setState { copy(createStepName = action.name) }
                }
            }
            is StepAction.StepItemAction.Create.ClickImeDone, StepAction.StepItemAction.Create.ClickSubmit -> {
                viewModelScope.launch(environment.dispatcher) {
                    if (state.value.validCreateStepName) {
                        environment.createStep(state.value.createStepName.text.trim(), state.value.task.id)
                        setState { copy(createStepName = TextFieldValue()) }
                    }
                }
            }
            is StepAction.StepItemAction.Create.OnShow -> {
                viewModelScope.launch {
                    setState { copy(createStepName = TextFieldValue()) }
                }
            }
        }
    }

    private fun handleStepItemEditAction(action: StepAction.StepItemAction.Edit) {
        when (action) {
            is StepAction.StepItemAction.Edit.ChangeStepName -> {
                viewModelScope.launch {
                    setState { copy(editStepName = action.name) }
                }
            }
            is StepAction.StepItemAction.Edit.ClickSave -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.updateStep(state.value.editStepName.text.trim(), action.stepId)
                    setState { copy(editStepName = TextFieldValue()) }
                }
            }
            is StepAction.StepItemAction.Edit.OnShow -> {
                viewModelScope.launch {
                    val step = state.value.task.steps.find { it.id == action.stepId }
                    if (step != null) {
                        setState { copy(editStepName = editStepName.copy(text = step.name, selection = TextRange(step.name.length))) }
                    }
                }
            }
            is StepAction.StepItemAction.Edit.OnToggleStatus -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.toggleStepStatus(action.step)
                }
            }
            is StepAction.StepItemAction.Edit.Delete -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.deleteStep(action.step)
                }
            }
        }
    }

}
