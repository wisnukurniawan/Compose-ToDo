package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoGroupReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoGroupWriteDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoListWriteDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.groupWithListToGroup
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toGroupDp
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toGroup
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

class ToDoGroupProvider @Inject constructor(
    @Named(DiName.DISPATCHER_IO) private val dispatcher: CoroutineDispatcher,
    private val toDoGroupWriteDao: ToDoGroupWriteDao,
    private val toDoListWriteDao: ToDoListWriteDao,
    private val toDoGroupReadDao: ToDoGroupReadDao,
) {

    fun getGroup(): Flow<List<ToDoGroup>> {
        return toDoGroupReadDao.getGroup()
            .filterNotNull()
            .map { it.toGroup() }
            .flowOn(dispatcher)
    }

    fun getGroup(groupId: String): Flow<ToDoGroup> {
        return toDoGroupReadDao.getGroup(groupId)
            .filterNotNull()
            .map { it.toGroup() }
            .flowOn(dispatcher)
    }

    fun getGroupWithList(): Flow<List<ToDoGroup>> {
        return toDoGroupReadDao.getGroupWithList()
            .filterNotNull()
            .map { it.groupWithListToGroup() }
            .flowOn(dispatcher)
    }

    suspend fun insertGroup(data: List<ToDoGroup>) {
        withContext(dispatcher) {
            toDoGroupWriteDao.insertGroup(data.toGroupDp())
        }
    }

    suspend fun ungroup(groupId: String, updatedAt: LocalDateTime, listIds: List<String>) {
        withContext(dispatcher) {
            toDoListWriteDao.updateListGroup(listIds, ToDoGroupDb.DEFAULT_ID, updatedAt)
            toDoGroupWriteDao.deleteGroup(groupId)
        }
    }

    suspend fun updateGroupName(id: String, name: String, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoGroupWriteDao.updateGroupName(id, name, updatedAt)
        }
    }

    suspend fun deleteGroup(id: String) {
        withContext(dispatcher) {
            toDoGroupWriteDao.deleteGroup(id)
        }
    }

}
