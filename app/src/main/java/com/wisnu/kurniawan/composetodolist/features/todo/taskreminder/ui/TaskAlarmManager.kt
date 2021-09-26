package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.wisnu.kurniawan.composetodolist.foundation.extension.toMillis
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.coreLogger.LoggrDebug
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun scheduleTaskAlarm(task: ToDoTask, time: LocalDateTime) {
        val receiverIntent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = TaskBroadcastReceiver.ACTION_ALARM_SHOW
            putExtra(TaskBroadcastReceiver.EXTRA_TASK_ID, task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.createdAt.toMillis().toInt(),
            receiverIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        setAlarm(time.toMillis(), pendingIntent)
    }

    fun cancelTaskAlarm(task: ToDoTask) {
        val receiverIntent = Intent(context, TaskBroadcastReceiver::class.java)
        receiverIntent.action = TaskBroadcastReceiver.ACTION_ALARM_SHOW

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.createdAt.toMillis().toInt(),
            receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        cancelAlarm(pendingIntent)
    }

    private fun setAlarm(
        triggerAtMillis: Long,
        operation: PendingIntent?
    ) {
        if (operation == null) {
            return
        }

        alarmManager?.let {
            LoggrDebug { "Alarm121 - setAlarm $triggerAtMillis" }

            AlarmManagerCompat.setAndAllowWhileIdle(it, AlarmManager.RTC_WAKEUP, triggerAtMillis, operation)
        }
    }

    private fun cancelAlarm(operation: PendingIntent?) {
        if (operation == null) {
            return
        }

        alarmManager?.cancel(operation)
    }

}
