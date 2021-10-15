package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupWithList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListWithTasks
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithSteps
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTaskOverallCount
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Return the most recent value for To-Do resource, null if it doesn’t exist.
 */
@Dao
interface ToDoReadDao {

    @Query("SELECT * FROM ToDoGroupDb")
    fun getGroup(): Flow<List<ToDoGroupDb>>

    @Query("SELECT * FROM ToDoGroupDb WHERE group_id = :groupId")
    fun getGroup(groupId: String): Flow<ToDoGroupDb>

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

    @Query("SELECT * FROM ToDoTaskDb")
    fun getTask(): Flow<List<ToDoTaskDb>>

    @Transaction
    @Query(
        """
            SELECT ToDoTaskDb.*, ToDoListDb.* 
            FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON task_listId = ToDoListDb.list_id
            WHERE task_dueDate IS NOT NULL
            ORDER BY task_dueDate ASC
            """
    )
    fun getTaskWithListOrderByDueDate(): Flow<List<ToDoTaskWithList>>

    @Transaction
    @Query(
        """
            SELECT ToDoTaskDb.*, ToDoListDb.*
            FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON task_listId = ToDoListDb.list_id
            WHERE 
            task_dueDate IS NOT NULL AND 
            task_status != "COMPLETE" AND 
            task_dueDate < :date 
            ORDER BY task_dueDate ASC
            """
    )
    fun getTaskWithListOrderByDueDateToday(date: LocalDateTime): Flow<List<ToDoTaskWithList>>

    @Transaction
    @Query(
        """
            SELECT COUNT(*) AS allTaskCount,
            (SELECT COUNT(task_dueDate) FROM ToDoTaskDb WHERE task_dueDate < :date AND task_status != "COMPLETE") AS scheduledTodayTaskCount, 
            COUNT(task_dueDate) AS scheduledTaskCount FROM ToDoTaskDb WHERE task_status != "COMPLETE"
        """
    )
    fun getTaskOverallCount(date: LocalDateTime): Flow<ToDoTaskOverallCount>

    @Query("SELECT * FROM ToDoTaskDb WHERE task_dueDate IS NOT NULL AND task_status != :status")
    fun getScheduledTasks(status: ToDoStatus = ToDoStatus.COMPLETE): Flow<List<ToDoTaskDb>>

    @Query("SELECT * FROM ToDoStepDb")
    fun getStep(): Flow<List<ToDoStepDb>>

    @Query("SELECT * FROM ToDoStepDb WHERE step_taskId = :taskId")
    fun getStep(taskId: String): Flow<List<ToDoStepDb>>

    @Transaction
    @Query("SELECT * FROM ToDoGroupDb")
    fun getGroupWithList(): Flow<List<ToDoGroupWithList>>

    @Transaction
    @Query("SELECT * FROM ToDoListDb")
    fun getListWithTasks(): Flow<List<ToDoListWithTasks>>

    @Transaction
    @Query("SELECT * FROM ToDoTaskDb WHERE task_name LIKE :query || '%' ORDER by task_listId")
    fun searchTaskWithList(query: String): Flow<List<ToDoTaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM ToDoListDb WHERE list_groupId = :groupId")
    fun getListWithTasks(groupId: String): Flow<List<ToDoListWithTasks>>

    @Transaction
    @Query("SELECT * FROM ToDoTaskDb")
    fun getTaskWithSteps(): Flow<List<ToDoTaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM ToDoTaskDb WHERE task_listId = :listId")
    fun getTaskWithSteps(listId: String): Flow<List<ToDoTaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM ToDoTaskDb WHERE task_id = :id")
    fun getTaskWithStepsById(id: String): Flow<ToDoTaskWithSteps>

    @Transaction
    @Query(
        """
            SELECT ToDoTaskDb.*, 
            ToDoListDb.*
            FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON task_listId = ToDoListDb.list_id
            WHERE ToDoTaskDb.task_id = :id
            """
    )
    fun getTaskWithListById(id: String): Flow<ToDoTaskWithList>

}
