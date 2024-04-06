package com.wisnu.kurniawan.composetodolist.features.todo.step.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoListProvider
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoStepProvider
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoTaskProvider
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggle
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class StepEnvironment @Inject constructor(
    private val toDoListProvider: ToDoListProvider,
    private val toDoTaskProvider: ToDoTaskProvider,
    private val toDoStepProvider: ToDoStepProvider,
    override val idProvider: IdProvider,
    override val dateTimeProvider: DateTimeProvider,
) : IStepEnvironment {

    override fun getTask(taskId: String, listId: String): Flow<Pair<ToDoTask, ToDoColor>> {
        return toDoTaskProvider.getTaskWithStepsById(taskId)
            .flatMapConcat { task ->
                toDoListProvider.getListById(listId)
                    .take(1)
                    .map { Pair(task, it.color) }
            }
    }

    override suspend fun deleteTask(task: ToDoTask) {
        toDoTaskProvider.deleteTaskById(task.id)
    }

    override suspend fun toggleTaskStatus(task: ToDoTask) {
        val currentDate = dateTimeProvider.now()
        task.toggleStatusHandler(
            currentDate,
            { completedAt, newStatus ->
                toDoTaskProvider.updateTaskStatus(task.id, newStatus, completedAt, currentDate)
            },
            { nextDueDate ->
                toDoTaskProvider.updateTaskDueDate(task.id, nextDueDate, task.isDueDateTimeSet, currentDate)
            }
        )
    }

    override suspend fun setRepeatTask(task: ToDoTask, toDoRepeat: ToDoRepeat) {
        toDoTaskProvider.updateTaskRepeat(task.id, toDoRepeat, dateTimeProvider.now())
    }

    override suspend fun toggleStepStatus(step: ToDoStep) {
        toDoStepProvider.updateStepStatus(step.id, step.status.toggle(), dateTimeProvider.now())
    }

    override suspend fun createStep(name: String, taskId: String) {
        val currentDate = dateTimeProvider.now()

        toDoStepProvider.insertStep(
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
        toDoStepProvider.deleteStepById(step.id)
    }

    override suspend fun updateTask(name: String, taskId: String) {
        toDoTaskProvider.updateTaskName(taskId, name, dateTimeProvider.now())
    }

    override suspend fun updateStep(name: String, stepId: String) {
        toDoStepProvider.updateStepName(stepId, name, dateTimeProvider.now())
    }

    override suspend fun updateTaskDueDate(
        date: LocalDateTime,
        isDueDateTimeSet: Boolean,
        taskId: String
    ) {
        toDoTaskProvider.updateTaskDueDate(taskId, date, isDueDateTimeSet, dateTimeProvider.now())
    }

    override suspend fun updateTaskNote(note: String, taskId: String) {
        val currentDate = dateTimeProvider.now()
        toDoTaskProvider.updateTaskNote(taskId, note, currentDate)
    }

    override suspend fun resetTaskDueDate(taskId: String) {
        toDoTaskProvider.resetTaskDueDate(taskId, dateTimeProvider.now())
    }

    override suspend fun resetTaskTime(date: LocalDateTime, taskId: String) {
        toDoTaskProvider.updateTaskDueDate(taskId, date, false, dateTimeProvider.now())
    }

}
