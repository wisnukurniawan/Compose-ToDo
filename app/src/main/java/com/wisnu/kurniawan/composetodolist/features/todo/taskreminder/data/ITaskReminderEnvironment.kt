package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ITaskReminderEnvironment {
    val dispatcher: CoroutineDispatcher
    val dateTimeProvider: DateTimeProvider
    fun getTask(taskId: String): Flow<ToDoTask>
    fun getTasksWithDueDate(): Flow<List<ToDoTask>>
    suspend fun toggleTaskStatus(taskId: String): Flow<ToDoTask>
}
