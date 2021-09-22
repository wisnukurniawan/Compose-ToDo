package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import java.time.LocalDateTime

/**
 * The omnibus create/update/delete, also known as last-write-wins.
 * Set the resource to the provided value.
 * If the provided value is null, delete it.
 * If two differing set operations arrive concurrently, pick a “latest” to win. Optionally, also return its previous value.
 */
@Dao
abstract class ToDoWriteDao {

    // Group section

    @Insert
    abstract suspend fun insertGroup(data: List<ToDoGroupDb>)

    @Delete
    abstract suspend fun deleteGroup(data: List<ToDoGroupDb>)

    @Query("DELETE FROM ToDoGroupDb WHERE id = :id")
    abstract suspend fun deleteGroup(id: String)

    @Transaction
    open suspend fun ungroup(groupId: String, updatedAt: LocalDateTime, listIds: List<String>) {
        updateListGroup(ids = listIds, groupId = ToDoGroupDb.DEFAULT_ID, updatedAt = updatedAt)
        deleteGroup(groupId)
    }

    @Query("UPDATE ToDoGroupDb SET name = :name, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateGroupName(id: String, name: String, updatedAt: LocalDateTime)

    @Transaction
    open suspend fun rearrangeGroup(data: List<ToDoGroupDb>) {
        deleteGroup(data)
        insertGroup(data)
    }

    // List section

    @Insert
    abstract suspend fun insertList(data: List<ToDoListDb>)

    @Delete
    abstract suspend fun deleteList(data: List<ToDoListDb>)

    @Query("DELETE FROM ToDoListDb WHERE id = :id")
    abstract suspend fun deleteListById(id: String)

    @Update
    abstract suspend fun updateList(data: List<ToDoListDb>)

    @Query("UPDATE ToDoListDb SET name = :name, color = :color, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateListNameAndColor(id: String, name: String, color: ToDoColor, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoListDb SET groupId = :groupId, updatedAt = :updatedAt WHERE id IN (:ids)")
    abstract suspend fun updateListGroup(ids: List<String>, groupId: String, updatedAt: LocalDateTime)

    @Transaction
    open suspend fun rearrangeList(data: List<ToDoListDb>) {
        deleteList(data)
        insertList(data)
    }

    // Task section

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTask(data: List<ToDoTaskDb>)

    @Delete
    abstract suspend fun deleteTask(data: List<ToDoTaskDb>)

    @Query("DELETE FROM ToDoTaskDb WHERE id = :id")
    abstract suspend fun deleteTaskById(id: String)

    @Query("UPDATE ToDoTaskDb SET name = :name, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateTaskName(id: String, name: String, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET dueDate = :dueDate, isDueDateTimeSet = :isDueDateTimeSet, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateTaskDueDate(id: String, dueDate: LocalDateTime?, isDueDateTimeSet: Boolean, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET dueDate = :dueDate, isDueDateTimeSet = :isDueDateTimeSet, repeat = :repeat, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun resetTaskDueDate(id: String, dueDate: LocalDateTime?, isDueDateTimeSet: Boolean, repeat: ToDoRepeat, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET status = :status, completedAt = :completedAt, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateTaskStatus(id: String, status: ToDoStatus, completedAt: LocalDateTime?, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET repeat = :repeat, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateTaskRepeat(id: String, repeat: ToDoRepeat, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET listId = :listId, updatedAt = :updatedAt WHERE id IN (:ids)")
    abstract suspend fun updateTaskList(ids: List<String>, listId: String, updatedAt: LocalDateTime)

    @Transaction
    open suspend fun rearrangeTask(data: List<ToDoTaskDb>) {
        deleteTask(data)
        insertTask(data)
    }

    // Step section

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertStep(data: List<ToDoStepDb>)

    @Delete
    abstract suspend fun deleteStep(data: List<ToDoStepDb>)

    @Query("UPDATE ToDoStepDb SET name = :name, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateStepName(id: String, name: String, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoStepDb SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    abstract suspend fun updateStepStatus(id: String, status: ToDoStatus, updatedAt: LocalDateTime)

    @Query("DELETE FROM ToDoStepDb WHERE id = :id")
    abstract suspend fun deleteStepById(id: String)

    @Transaction
    open suspend fun rearrangeStep(data: List<ToDoStepDb>) {
        deleteStep(data)
        insertStep(data)
    }
}
