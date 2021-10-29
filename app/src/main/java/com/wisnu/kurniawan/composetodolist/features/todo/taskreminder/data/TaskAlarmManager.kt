package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.ui.TaskBroadcastReceiver
import com.wisnu.kurniawan.composetodolist.foundation.extension.toMillis
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    fun scheduleTaskAlarm(task: ToDoTask, time: LocalDateTime) {
        val receiverIntent = Intent(context, TaskBroadcastReceiver::class.java).apply {
            action = TaskBroadcastReceiver.ACTION_ALARM_SHOW
            putExtra(TaskBroadcastReceiver.EXTRA_TASK_ID, task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.createdAt.toMillis().toInt(),
            receiverIntent,
            flags
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
            flags
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
