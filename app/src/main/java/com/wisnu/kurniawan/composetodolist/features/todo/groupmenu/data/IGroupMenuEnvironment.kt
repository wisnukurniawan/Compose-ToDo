package com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.data

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IGroupMenuEnvironment {
    val dispatcher: CoroutineDispatcher
    val dateTimeGenerator: DateTimeGenerator
    suspend fun deleteGroup(groupId: String): Flow<Any>
    fun hasList(groupId: String): Flow<Boolean>
}
