package com.wisnu.kurniawan.composetodolist.features.todo.main.data

import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IToDoMainEnvironment {
    val dispatcher: CoroutineDispatcher
    fun getGroup(): Flow<List<ToDoGroup>>
    suspend fun deleteList(list: ToDoList)
}
