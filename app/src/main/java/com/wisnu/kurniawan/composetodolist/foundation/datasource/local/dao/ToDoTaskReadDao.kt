package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithSteps
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTaskOverallCount
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ToDoTaskReadDao {

    @Query("SELECT * FROM ToDoTaskDb")
    fun getTask(): Flow<List<ToDoTaskDb>>

    @Transaction
    @Query(
        """
            SELECT *
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
            SELECT *
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

    @Transaction
    @Query(
        """
            SELECT *
            FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON task_listId = ToDoListDb.list_id
            JOIN ToDoTaskFtsDb ON ToDoTaskDb.task_name = ToDoTaskFtsDb.task_name
            WHERE ToDoTaskFtsDb MATCH :query
            """
    )
    fun searchTaskWithList(query: String): Flow<List<ToDoTaskWithList>>

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
            SELECT *
            FROM ToDoTaskDb 
            LEFT JOIN ToDoListDb ON task_listId = ToDoListDb.list_id
            WHERE ToDoTaskDb.task_id = :id
            """
    )
    fun getTaskWithListById(id: String): Flow<ToDoTaskWithList>

}
