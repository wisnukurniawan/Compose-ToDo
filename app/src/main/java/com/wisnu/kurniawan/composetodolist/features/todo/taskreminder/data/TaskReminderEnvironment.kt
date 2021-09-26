package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Named

class TaskReminderEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    override val dateTimeProvider: DateTimeProvider,
    private val localManager: LocalManager,
) : ITaskReminderEnvironment {

    override fun getTask(taskId: String): Flow<ToDoTask> {
        return localManager.getTaskWithStepsById(taskId)
            .take(1)
            .filter {
                it.status != ToDoStatus.COMPLETE &&
                    it.dueDate != null
            }
    }

    override fun getTasksWithDueDate(): Flow<List<ToDoTask>> {
        return localManager.getTasksWithDueDate()
            .take(1)
            .map { tasks ->
                tasks.filter { it.status != ToDoStatus.COMPLETE }
            }
    }

    override suspend fun toggleTaskStatus(taskId: String): Flow<ToDoTask> {
        return getTask(taskId)
            .onEach { task ->
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
    }

}
