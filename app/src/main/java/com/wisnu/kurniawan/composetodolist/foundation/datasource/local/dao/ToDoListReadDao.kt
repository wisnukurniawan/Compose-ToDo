package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoListReadDao {

    @Query("SELECT * FROM ToDoListDb")
    fun getList(): Flow<List<ToDoListDb>>

    @Query("SELECT * FROM ToDoListDb WHERE list_groupId = 'default' OR list_groupId = :groupId")
    fun getListWithUnGroupList(groupId: String): Flow<List<ToDoListDb>>

    @Query("SELECT * FROM ToDoListDb WHERE list_id = :id")
    fun getListById(id: String): Flow<ToDoListDb>

    @Query("SELECT * FROM ToDoListDb WHERE list_groupId = :groupId")
    fun getListByGroupId(groupId: String): Flow<List<ToDoListDb>>

    @Transaction
    @Query("SELECT * FROM ToDoListDb WHERE list_id = :id")
    fun getListWithTasksById(id: String): Flow<ToDoListWithTasks>

    @Transaction
    @Query("SELECT * FROM ToDoListDb")
    fun getListWithTasks(): Flow<List<ToDoListWithTasks>>

    @Transaction
    @Query("SELECT * FROM ToDoListDb WHERE list_groupId = :groupId")
    fun getListWithTasks(groupId: String): Flow<List<ToDoListWithTasks>>

}
