package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ITaskReminderEnvironment {
    val dispatcher: CoroutineDispatcher
    fun notifyNotification(taskId: String): Flow<TaskWithList>
    fun snoozeReminder(taskId: String): Flow<TaskWithList>
    suspend fun completeReminder(taskId: String): Flow<TaskWithList>
    fun restartAllReminder(): Flow<List<ToDoTask>>
}
