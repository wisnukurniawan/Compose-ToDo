package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ITaskReminderEnvironment {
    val dispatcher: CoroutineDispatcher
    fun getTask(taskId: String): Flow<ToDoTask>
}
