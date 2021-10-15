package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface IScheduledEnvironment {
    val idProvider: IdProvider
    val dateTimeProvider: DateTimeProvider
    val dispatcher: CoroutineDispatcher
    fun getToDoTaskWithStepsOrderByDueDateWithList(maxDate: LocalDateTime? = null): Flow<List<TaskWithList>>
    suspend fun toggleTaskStatus(toDoTask: ToDoTask)
    suspend fun deleteTask(task: ToDoTask)
}
