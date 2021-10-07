package com.wisnu.kurniawan.composetodolist.features.todo.main.data

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTaskOverallCount
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IToDoMainEnvironment {
    val dispatcher: CoroutineDispatcher
    val dateTimeProvider: DateTimeProvider
    fun getGroup(): Flow<List<ToDoGroup>>
    fun getOverallCount(): Flow<ToDoTaskOverallCount>
    suspend fun deleteList(list: ToDoList)
}
