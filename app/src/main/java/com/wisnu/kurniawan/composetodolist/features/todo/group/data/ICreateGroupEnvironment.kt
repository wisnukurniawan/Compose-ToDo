package com.wisnu.kurniawan.composetodolist.features.todo.group.data

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdGenerator
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICreateGroupEnvironment {
    val dispatcher: CoroutineDispatcher
    val idGenerator: IdGenerator
    val dateTimeGenerator: DateTimeGenerator
    suspend fun getGroup(groupId: String): Flow<ToDoGroup>
    suspend fun createGroup(name: String): Flow<String>
    suspend fun renameGroup(groupId: String, name: String): Flow<Any>
}
