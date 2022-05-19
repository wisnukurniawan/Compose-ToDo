package com.wisnu.kurniawan.composetodolist.features.todo.step.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggle
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(FlowPreview::class)
class StepEnvironment @Inject constructor(
    private val localManager: LocalManager,
    override val idProvider: IdProvider,
    override val dateTimeProvider: DateTimeProvider,
) : IStepEnvironment {

    override fun getTask(taskId: String, listId: String): Flow<Pair<ToDoTask, ToDoColor>> {
        return localManager.getTaskWithStepsById(taskId)
            .flatMapConcat { task ->
                localManager.getListById(listId)
                    .take(1)
                    .map { Pair(task, it.color) }
            }
    }

    override suspend fun deleteTask(task: ToDoTask) {
        localManager.deleteTaskById(task.id)
    }

    override suspend fun toggleTaskStatus(task: ToDoTask) {
        val currentDate = dateTimeProvider.now()
        task.toggleStatusHandler(
            currentDate,
            { completedAt, newStatus ->
                localManager.updateTaskStatus(task.id, newStatus, completedAt, currentDate)
            },
            { nextDueDate ->
                localManager.updateTaskDueDate(task.id, nextDueDate, task.isDueDateTimeSet, currentDate)
            }
        )
    }

    override suspend fun setRepeatTask(task: ToDoTask, toDoRepeat: ToDoRepeat) {
        localManager.updateTaskRepeat(task.id, toDoRepeat, dateTimeProvider.now())
    }

    override suspend fun toggleStepStatus(step: ToDoStep) {
        localManager.updateStepStatus(step.id, step.status.toggle(), dateTimeProvider.now())
    }

    override suspend fun createStep(name: String, taskId: String) {
        val currentDate = dateTimeProvider.now()

        localManager.insertStep(
            listOf(
                ToDoStep(
                    id = idProvider.generate(),
                    name = name,
                    createdAt = currentDate,
                    updatedAt = currentDate
                )
            ),
            taskId
        )
    }

    override suspend fun deleteStep(step: ToDoStep) {
        localManager.deleteStepById(step.id)
    }

    override suspend fun updateTask(name: String, taskId: String) {
        localManager.updateTaskName(taskId, name, dateTimeProvider.now())
    }

    override suspend fun updateStep(name: String, stepId: String) {
        localManager.updateStepName(stepId, name, dateTimeProvider.now())
    }

    override suspend fun updateTaskDueDate(
        date: LocalDateTime,
        isDueDateTimeSet: Boolean,
        taskId: String
    ) {
        localManager.updateTaskDueDate(taskId, date, isDueDateTimeSet, dateTimeProvider.now())
    }

    override suspend fun updateTaskNote(note: String, taskId: String) {
        val currentDate = dateTimeProvider.now()
        localManager.updateTaskNote(taskId, note, currentDate)
    }

    override suspend fun resetTaskDueDate(taskId: String) {
        localManager.resetTaskDueDate(taskId, dateTimeProvider.now())
    }

    override suspend fun resetTaskTime(date: LocalDateTime, taskId: String) {
        localManager.updateTaskDueDate(taskId, date, false, dateTimeProvider.now())
    }

}
