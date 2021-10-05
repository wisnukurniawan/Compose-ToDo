package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ITaskReminderEnvironment {
    val dispatcher: CoroutineDispatcher
    fun notifyNotification(taskId: String): Flow<Pair<ToDoTask, ToDoList>>
    fun snoozeReminder(taskId: String): Flow<Pair<ToDoTask, String>>
    suspend fun completeReminder(taskId: String): Flow<Pair<ToDoTask, String>>
    fun restartAllReminder(): Flow<List<ToDoTask>>
}
