package com.wisnu.kurniawan.composetodolist.features.todo.grouplist.data

import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IUpdateGroupListEnvironment {
    val dispatcher: CoroutineDispatcher
    val dateTimeGenerator: DateTimeGenerator
    fun getListWithUnGroupList(groupId: String): Flow<List<GroupIdWithList>>
    suspend fun updateList(data: List<GroupIdWithList>)
}
