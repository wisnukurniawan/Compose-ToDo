package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.getNextScheduledDueDate
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.coreLogger.Loggr
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Named

class TaskReminderEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val dateTimeProvider: DateTimeProvider,
    private val localManager: LocalManager,
    private val alarmManager: TaskAlarmManager,
    private val notificationManager: TaskNotificationManager
) : ITaskReminderEnvironment {

    @OptIn(FlowPreview::class)
    override fun notifyNotification(taskId: String): Flow<Pair<ToDoTask, ToDoList>> {
        return getTask(taskId)
            .flatMapConcat { (task, listId) ->
                localManager.getListById(listId)
                    .take(1)
                    .map { Pair(task, it) }
            }
            .onEach { (task, list) ->
                Loggr.debug("AlarmFlow") { "AlarmShow $task $list" }
                notificationManager.show(task, list)
            }
    }

    override fun snoozeReminder(taskId: String): Flow<Pair<ToDoTask, String>> {
        return getTask(taskId)
            .onEach { (task, _) ->
                alarmManager.scheduleTaskAlarm(task, dateTimeProvider.now().plusMinutes(15))
                notificationManager.dismiss(task)
            }
    }

    override suspend fun completeReminder(taskId: String): Flow<Pair<ToDoTask, String>> {
        return getTask(taskId)
            .onEach { (task, _) ->
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

                alarmManager.cancelTaskAlarm(task)
                notificationManager.dismiss(task)
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

    private fun getTask(taskId: String): Flow<Pair<ToDoTask, String>> {
        return localManager.getTaskWithStepsByIdWithListId(taskId)
            .take(1)
            .filter { (task, _) ->
                task.status != ToDoStatus.COMPLETE &&
                    task.dueDate != null
            }
    }

}
