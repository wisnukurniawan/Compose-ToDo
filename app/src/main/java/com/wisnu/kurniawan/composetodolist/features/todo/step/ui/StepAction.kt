package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.ui.text.input.TextFieldValue
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import java.time.LocalDate
import java.time.LocalTime

sealed class StepAction {
    sealed class TaskAction : StepAction() {
        object ClickSave : TaskAction()
        object OnShow : TaskAction()
        object OnToggleStatus : TaskAction()
        object Delete : TaskAction()
        data class ChangeTaskName(val name: TextFieldValue) : TaskAction()
        data class SelectRepeat(val repeatItem: ToDoRepeatItem) : TaskAction()
        data class SelectDueDate(val date: LocalDate) : TaskAction()
        data class SelectDueTime(val selected: Boolean) : TaskAction()
        object EditDueTime : TaskAction()
        object DismissDueTimePicker : TaskAction()
        data class SelectTime(val time: LocalTime) : TaskAction()
        object ResetDueDate : TaskAction()
    }

    sealed class StepItemAction : StepAction() {
        sealed class Edit : StepItemAction() {
            data class ClickSave(val stepId: String) : Edit()
            data class OnShow(val stepId: String) : Edit()
            data class ChangeStepName(val name: TextFieldValue) : Edit()

            data class OnToggleStatus(val step: ToDoStep) : Edit()
            data class Delete(val step: ToDoStep) : Edit()
        }

        sealed class Create : StepItemAction() {
            object ClickSubmit : Create()
            object ClickImeDone : Create()
            object OnShow : Create()
            data class ChangeStepName(val name: TextFieldValue) : Create()
        }
    }

    sealed class NoteAction : StepAction() {
        object OnShow : NoteAction()
        data class ChangeNote(val note: TextFieldValue) : NoteAction()
    }
}
