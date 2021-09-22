package com.wisnu.kurniawan.composetodolist.features.todo.step.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoTask

@Immutable
data class StepState(
    val task: ToDoTask = ToDoTask(
        createdAt = DateTimeGenerator.now(),
        updatedAt = DateTimeGenerator.now()
    ),
    val color: ToDoColor = ToDoColor.BLUE,
    val editTaskName: TextFieldValue = TextFieldValue(),
    val editStepName: TextFieldValue = TextFieldValue(),
    val createStepName: TextFieldValue = TextFieldValue(),
    val repeatItems: List<ToDoRepeatItem> = initialRepeatItem()
) {
    val validEditTaskName = editTaskName.text.isNotBlank()
    val validEditStepName = editStepName.text.isNotBlank()
    val validCreateStepName = createStepName.text.isNotBlank()

    companion object {
        private fun initialRepeatItem(): List<ToDoRepeatItem> {
            return listOf(
                ToDoRepeatItem(
                    ToDoRepeat.NEVER,
                    false
                ),
                ToDoRepeatItem(
                    ToDoRepeat.DAILY,
                    false
                ),
                ToDoRepeatItem(
                    ToDoRepeat.WEEKDAYS,
                    false
                ),
                ToDoRepeatItem(
                    ToDoRepeat.WEEKLY,
                    false
                ),
                ToDoRepeatItem(
                    ToDoRepeat.MONTHLY,
                    false
                ),
                ToDoRepeatItem(
                    ToDoRepeat.YEARLY,
                    false
                )
            )
        }
    }
}

data class ToDoRepeatItem(
    val repeat: ToDoRepeat,
    val applied: Boolean
)
