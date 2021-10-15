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

    @Query("SELECT * FROM ToDoGroupDb WHERE id = :groupId")
    fun getGroup(groupId: String): Flow<ToDoGroupDb>

    @Query("SELECT * FROM ToDoListDb")
    fun getList(): Flow<List<ToDoListDb>>

    @Query("SELECT * FROM ToDoListDb WHERE groupId = 'default' OR groupId = :groupId")
    fun getListWithUnGroupList(groupId: String): Flow<List<ToDoListDb>>

    @Query("SELECT * FROM ToDoListDb WHERE id = :id")
    fun getListById(id: String): Flow<ToDoListDb>

    @Query("SELECT * FROM ToDoListDb WHERE groupId = :groupId")
    fun getListByGroupId(groupId: String): Flow<List<ToDoListDb>>

    @Transaction
    @Query("SELECT * FROM ToDoListDb WHERE id = :id")
    fun getListWithTasksById(id: String): Flow<ToDoListWithTasks>

    @Query("SELECT * FROM ToDoTaskDb")
    fun getTask(): Flow<List<ToDoTaskDb>>

    @Transaction
    @Query(
        """
            SELECT ToDoTaskDb.*, 
            ToDoListDb.id AS list_id,
            ToDoListDb.name AS list_name,
            ToDoListDb.color AS list_color,
            ToDoListDb.groupId AS list_groupId,
            ToDoListDb.createdAt AS list_createdAt,
            ToDoListDb.updatedAt AS list_updatedAt FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON listId = ToDoListDb.id
            WHERE 
            dueDate IS NOT NULL
            ORDER BY dueDate ASC
            """
    )
    fun getTaskWithListOrderByDueDate(): Flow<List<ToDoTaskWithList>>

    @Transaction
    @Query(
        """
            SELECT ToDoTaskDb.*, 
            ToDoListDb.id AS list_id,
            ToDoListDb.name AS list_name,
            ToDoListDb.color AS list_color,
            ToDoListDb.groupId AS list_groupId,
            ToDoListDb.createdAt AS list_createdAt,
            ToDoListDb.updatedAt AS list_updatedAt
            FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON listId = ToDoListDb.id
            WHERE 
            dueDate IS NOT NULL AND 
            status != "COMPLETE" AND 
            dueDate < :date 
            ORDER BY dueDate ASC
            """
    )
    fun getTaskWithListOrderByDueDateToday(date: LocalDateTime): Flow<List<ToDoTaskWithList>>

    @Transaction
    @Query(
        """
            SELECT COUNT(*) AS allTaskCount,
            (SELECT COUNT(dueDate) FROM ToDoTaskDb WHERE dueDate < :date AND status != "COMPLETE") AS scheduledTodayTaskCount, 
            COUNT(dueDate) AS scheduledTaskCount FROM ToDoTaskDb WHERE status != "COMPLETE"
        """
    )
    fun getTaskOverallCount(date: LocalDateTime): Flow<ToDoTaskOverallCount>

    @Query("SELECT * FROM ToDoTaskDb WHERE dueDate IS NOT NULL AND status != :status")
    fun getScheduledTasks(status: ToDoStatus = ToDoStatus.COMPLETE): Flow<List<ToDoTaskDb>>

    @Query("SELECT * FROM ToDoStepDb")
    fun getStep(): Flow<List<ToDoStepDb>>

    @Query("SELECT * FROM ToDoStepDb WHERE taskId = :taskId")
    fun getStep(taskId: String): Flow<List<ToDoStepDb>>

    @Transaction
    @Query("SELECT * FROM ToDoGroupDb")
    fun getGroupWithList(): Flow<List<ToDoGroupWithList>>

    @Transaction
    @Query("SELECT * FROM ToDoListDb")
    fun getListWithTasks(): Flow<List<ToDoListWithTasks>>

    @Transaction
    @Query("SELECT * FROM ToDoListDb WHERE groupId = :groupId")
    fun getListWithTasks(groupId: String): Flow<List<ToDoListWithTasks>>

    @Transaction
    @Query("SELECT * FROM ToDoTaskDb")
    fun getTaskWithSteps(): Flow<List<ToDoTaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM ToDoTaskDb WHERE listId = :listId")
    fun getTaskWithSteps(listId: String): Flow<List<ToDoTaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM ToDoTaskDb WHERE id = :id")
    fun getTaskWithStepsById(id: String): Flow<ToDoTaskWithSteps>

    @Transaction
    @Query(
        """
            SELECT ToDoTaskDb.*, 
            ToDoListDb.id AS list_id,
            ToDoListDb.name AS list_name,
            ToDoListDb.color AS list_color,
            ToDoListDb.groupId AS list_groupId,
            ToDoListDb.createdAt AS list_createdAt,
            ToDoListDb.updatedAt AS list_updatedAt
            FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON listId = ToDoListDb.id
            WHERE ToDoTaskDb.id = :id
            """
    )
    fun getTaskWithListById(id: String): Flow<ToDoTaskWithList>

}
