package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.ui

import com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data.ITaskReminderEnvironment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class TaskReminderViewModel @Inject constructor(
    private val environment: ITaskReminderEnvironment,
) {

    // TODO e2e
    fun dispatch(action: TaskReminderAction) {
        when (action) {
            is TaskReminderAction.AlarmShow -> {
                GlobalScope.launch {
                    environment.notifyNotification(action.taskId)
                        .collect()
                }
            }
            TaskReminderAction.AppBootCompleted -> {
                GlobalScope.launch {
                    environment.restartAllReminder()
                        .collect()
                }
            }
            is TaskReminderAction.NotificationCompleted -> {
                GlobalScope.launch {
                    environment.completeReminder(action.taskId)
                        .collect()
                }
            }
            is TaskReminderAction.NotificationSnooze -> {
                GlobalScope.launch {
                    environment.snoozeReminder(action.taskId)
                        .collect()
                }
            }
        }
    }

}
