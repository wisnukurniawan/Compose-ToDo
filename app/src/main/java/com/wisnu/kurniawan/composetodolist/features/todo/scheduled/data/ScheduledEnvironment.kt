package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoGroupProvider
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoTaskProvider
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDateTime
import javax.inject.Inject

class ScheduledEnvironment @Inject constructor(
    private val toDoTaskProvider: ToDoTaskProvider,
    override val idProvider: IdProvider,
    override val dateTimeProvider: DateTimeProvider,
) : IScheduledEnvironment {

    override fun getToDoTaskWithStepsOrderByDueDateWithList(maxDate: LocalDateTime?): Flow<List<TaskWithList>> {
        val operation = if (maxDate != null) {
            toDoTaskProvider.getTaskWithListOrderByDueDateToday(maxDate)
        } else {
            toDoTaskProvider.getTaskWithListOrderByDueDate()
        }

        return operation
            .distinctUntilChanged()
    }

    override suspend fun toggleTaskStatus(toDoTask: ToDoTask) {
        val currentDate = dateTimeProvider.now()
        toDoTask.toggleStatusHandler(
            currentDate,
            { completedAt, newStatus ->
                toDoTaskProvider.updateTaskStatus(toDoTask.id, newStatus, completedAt, currentDate)
            },
            { nextDueDate ->
                toDoTaskProvider.updateTaskDueDate(toDoTask.id, nextDueDate, toDoTask.isDueDateTimeSet, currentDate)
            }
        )
    }

    override suspend fun deleteTask(task: ToDoTask) {
        toDoTaskProvider.deleteTaskById(task.id)
    }

}
