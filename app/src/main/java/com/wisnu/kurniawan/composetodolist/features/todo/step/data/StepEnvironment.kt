package com.wisnu.kurniawan.composetodolist.features.todo.step.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.newStatus
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdGenerator
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

@OptIn(FlowPreview::class)
class StepEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val localManager: LocalManager,
    override val idGenerator: IdGenerator,
    override val dateTimeGenerator: DateTimeGenerator,
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
        val currentDate = dateTimeGenerator.now()
        val newStatus = task.newStatus()
        val completedAt = when (newStatus) {
            ToDoStatus.IN_PROGRESS -> null
            ToDoStatus.COMPLETE -> currentDate
        }
        localManager.updateTaskStatus(task.id, newStatus, completedAt, currentDate)
    }

    override suspend fun setRepeatTask(task: ToDoTask, toDoRepeat: ToDoRepeat) {
        localManager.updateTaskRepeat(task.id, toDoRepeat, dateTimeGenerator.now())
    }

    override suspend fun toggleStepStatus(step: ToDoStep) {
        localManager.updateStepStatus(step.id, step.newStatus(), dateTimeGenerator.now())
    }

    override suspend fun createStep(name: String, taskId: String) {
        val currentDate = dateTimeGenerator.now()

        localManager.insertStep(
            listOf(
                ToDoStep(
                    id = idGenerator.generate(),
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
        localManager.updateTaskName(taskId, name, dateTimeGenerator.now())
    }

    override suspend fun updateStep(name: String, stepId: String) {
        localManager.updateStepName(stepId, name, dateTimeGenerator.now())
    }

    override suspend fun updateTaskDueDate(
        date: LocalDateTime,
        isDueDateTimeSet: Boolean,
        taskId: String
    ) {
        localManager.updateTaskDueDate(taskId, date, isDueDateTimeSet, dateTimeGenerator.now())
    }

    override suspend fun resetTaskDueDate(taskId: String) {
        localManager.resetTaskDueDate(taskId, dateTimeGenerator.now())
    }

    override suspend fun resetTaskTime(date: LocalDateTime, taskId: String) {
        localManager.updateTaskDueDate(taskId, date, false, dateTimeGenerator.now())
    }

}
