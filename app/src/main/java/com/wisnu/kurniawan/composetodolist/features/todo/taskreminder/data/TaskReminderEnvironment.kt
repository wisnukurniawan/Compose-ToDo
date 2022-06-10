package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.extension.getNextScheduledDueDate
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.coreLogger.Loggr
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class TaskReminderEnvironment @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val localManager: LocalManager,
    private val alarmManager: TaskAlarmManager,
    private val notificationManager: TaskNotificationManager
) : ITaskReminderEnvironment {

    override fun notifyNotification(taskId: String): Flow<TaskWithList> {
        return getTask(taskId)
            .onEach { (list, task) ->
                Loggr.debug("AlarmFlow") { "AlarmShow $task $list" }
                notificationManager.show(task, list)
            }
    }

    override fun snoozeReminder(taskId: String): Flow<TaskWithList> {
        return getTask(taskId)
            .onEach { task ->
                alarmManager.scheduleTaskAlarm(task.task, dateTimeProvider.now().plusMinutes(15))
                notificationManager.dismiss(task.task)
            }
    }

    override suspend fun completeReminder(taskId: String): Flow<TaskWithList> {
        return getTask(taskId)
            .onEach { task ->
                val currentDate = dateTimeProvider.now()
                task.task.toggleStatusHandler(
                    currentDate,
                    { completedAt, newStatus ->
                        localManager.updateTaskStatus(task.task.id, newStatus, completedAt, currentDate)
                    },
                    { nextDueDate ->
                        localManager.updateTaskDueDate(task.task.id, nextDueDate, task.task.isDueDateTimeSet, currentDate)
                    }
                )

                alarmManager.cancelTaskAlarm(task.task)
                notificationManager.dismiss(task.task)
            }
    }

    override fun restartAllReminder(): Flow<List<ToDoTask>> {
        return localManager.getScheduledTasks()
            .take(1)
            .onEach { tasks ->
                tasks.forEach {
                    alarmManager.scheduleTaskAlarm(it, it.getNextScheduledDueDate(dateTimeProvider.now()))
                }
            }
    }

    private fun getTask(taskId: String): Flow<TaskWithList> {
        return localManager.getTaskWithListById(taskId)
            .take(1)
            .filter { task ->
                task.task.status != ToDoStatus.COMPLETE &&
                    task.task.dueDate != null
            }
    }

}
