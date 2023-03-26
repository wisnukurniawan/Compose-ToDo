package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.detail.ui.select
import com.wisnu.kurniawan.composetodolist.features.todo.step.data.IStepEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.extension.DEFAULT_TASK_LOCAL_TIME
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_LIST_ID
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_TASK_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class StepViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stepEnvironment: IStepEnvironment
) :
    StatefulViewModel<StepState, StepEffect, StepAction, IStepEnvironment>(StepState(), stepEnvironment) {

    private val taskId = savedStateHandle.get<String>(ARG_TASK_ID)
    private val listId = savedStateHandle.get<String>(ARG_LIST_ID)

    private var updateNoteJob: Job? = null

    init {
        initTask()
    }

    private fun initTask() {
        viewModelScope.launch {
            if (taskId != null && listId != null) {
                environment.getTask(taskId, listId)
                    .collect { (task, color) ->
                        setState { copy(task = task, color = color, repeatItems = repeatItems.select(task.repeat)) }
                    }
            }
        }
    }

    override fun dispatch(action: StepAction) {
        when (action) {
            is StepAction.TaskAction -> handleTaskAction(action)
            is StepAction.StepItemAction -> handleStepItemAction(action)
            is StepAction.NoteAction -> handleStepNoteAction(action)
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
                viewModelScope.launch {
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
                viewModelScope.launch {
                    environment.toggleTaskStatus(state.value.task)
                }
            }
            StepAction.TaskAction.Delete -> {
                viewModelScope.launch {
                    environment.deleteTask(state.value.task)
                }
            }
            is StepAction.TaskAction.SelectRepeat -> {
                viewModelScope.launch {
                    environment.setRepeatTask(state.value.task, action.repeatItem.repeat)
                }
            }
            StepAction.TaskAction.ResetDueDate -> {
                viewModelScope.launch {
                    environment.resetTaskDueDate(state.value.task.id)
                }
            }
            is StepAction.TaskAction.SelectDueDate -> {
                viewModelScope.launch {
                    val newDateTime = state.value.task.updatedDate(action.date)
                    environment.updateTaskDueDate(newDateTime, state.value.task.isDueDateTimeSet, state.value.task.id)
                }
            }
            is StepAction.TaskAction.SelectTime -> {
                viewModelScope.launch {
                    setState { copy(showDueTimePicker = false) }

                    val newDateTime = state.value.task.updatedTime(environment.dateTimeProvider.now().toLocalDate(), action.time)
                    environment.updateTaskDueDate(newDateTime, true, state.value.task.id)
                }
            }
            StepAction.TaskAction.DismissDueTimePicker -> {
                viewModelScope.launch {
                    setState { copy(showDueTimePicker = false) }
                }
            }
            StepAction.TaskAction.EditDueTime -> {
                val initial = state.value.task.dueDate?.toLocalTime()

                if (initial != null) {
                    setState { copy(showDueTimePicker = true, dueDateInitial = initial) }
                }
            }
            is StepAction.TaskAction.SelectDueTime -> {
                viewModelScope.launch {
                    if (action.selected) {
                        val nextHour = environment.dateTimeProvider.now().toLocalTime().plusHours(1)
                        val initial = LocalTime.of(nextHour.hour, 0)
                        setState { copy(showDueTimePicker = true, dueDateInitial = initial) }
                    } else {
                        val newDateTime = state.value.task.updatedTime(environment.dateTimeProvider.now().toLocalDate(), DEFAULT_TASK_LOCAL_TIME)
                        environment.resetTaskTime(newDateTime, state.value.task.id)
                    }
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
                viewModelScope.launch {
                    if (state.value.validCreateStepName) {
                        environment.createStep(state.value.createStepName.text.trim(), state.value.task.id)
                        setState { copy(createStepName = TextFieldValue()) }

                        val lastIndexStep = state.value.task.steps.size
                        setEffect(StepEffect.ScrollTo(lastIndexStep))
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
                viewModelScope.launch {
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
                viewModelScope.launch {
                    environment.toggleStepStatus(action.step)
                }
            }
            is StepAction.StepItemAction.Edit.Delete -> {
                viewModelScope.launch {
                    environment.deleteStep(action.step)
                }
            }
        }
    }

    private fun handleStepNoteAction(action: StepAction.NoteAction) {
        when (action) {
            is StepAction.NoteAction.ChangeNote -> {
                updateNoteJob?.cancel()
                updateNoteJob = viewModelScope.launch {
                    setState { copy(editNote = action.note) }
                    delay(250)
                    environment.updateTaskNote(state.value.editNote.text, state.value.task.id)
                }
            }
            is StepAction.NoteAction.OnShow -> {
                viewModelScope.launch {
                    val note = state.value.task.note
                    setState { copy(editNote = editNote.copy(text = note, selection = TextRange(note.length))) }
                }
            }
        }
    }

}

fun ToDoTask.isDueDateSet(): Boolean = this.dueDate != null

fun ToDoTask.updatedDate(newLocalDate: LocalDate): LocalDateTime {
    val localTime = dueDate?.toLocalTime() ?: DEFAULT_TASK_LOCAL_TIME
    return LocalDateTime.of(newLocalDate, localTime)
}

fun ToDoTask.updatedTime(defaultDate: LocalDate, newLocalTime: LocalTime): LocalDateTime {
    val localDate = dueDate?.toLocalDate() ?: defaultDate
    return LocalDateTime.of(localDate, newLocalTime)
}

