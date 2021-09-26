package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.ui

sealed class TaskReminderAction {
    data class AlarmShow(val taskId: String) : TaskReminderAction()
    data class NotificationCompleted(val taskId: String) : TaskReminderAction()
    data class NotificationSnooze(val taskId: String) : TaskReminderAction()
    object AppBootCompleted : TaskReminderAction()
}
