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
    private val notificationManager: TaskNotificationManager
) {

    fun dispatch(action: TaskReminderAction) {
        when (action) {
            is TaskReminderAction.AlarmShow -> {
                GlobalScope.launch(environment.dispatcher) {
                    environment.getTask(action.taskId)
                        .collect {
                            notificationManager.show(it)
                        }
                }
            }
            TaskReminderAction.AppBootCompleted -> {
                GlobalScope.launch(environment.dispatcher) {

                }
            }
            is TaskReminderAction.NotificationCompleted -> {
                GlobalScope.launch(environment.dispatcher) {

                }
            }
            is TaskReminderAction.NotificationSnooze -> {
                GlobalScope.launch(environment.dispatcher) {

                }
            }
        }
    }

}
