package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import java.time.LocalDateTime

@Dao
abstract class ToDoTaskWriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTask(data: List<ToDoTaskDb>)

    @Delete
    abstract suspend fun deleteTask(data: List<ToDoTaskDb>)

    @Query("DELETE FROM ToDoTaskDb WHERE task_id = :id")
    abstract suspend fun deleteTaskById(id: String)

    @Query("UPDATE ToDoTaskDb SET task_name = :name, task_updatedAt = :updatedAt WHERE task_id = :id")
    abstract suspend fun updateTaskName(id: String, name: String, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET task_dueDate = :dueDate, task_isDueDateTimeSet = :isDueDateTimeSet, task_updatedAt = :updatedAt WHERE task_id = :id")
    abstract suspend fun updateTaskDueDate(id: String, dueDate: LocalDateTime?, isDueDateTimeSet: Boolean, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET task_dueDate = :dueDate, task_isDueDateTimeSet = :isDueDateTimeSet, task_repeat = :repeat, task_updatedAt = :updatedAt WHERE task_id = :id")
    abstract suspend fun resetTaskDueDate(id: String, dueDate: LocalDateTime?, isDueDateTimeSet: Boolean, repeat: ToDoRepeat, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET task_status = :status, task_completedAt = :completedAt, task_updatedAt = :updatedAt WHERE task_id = :id")
    abstract suspend fun updateTaskStatus(id: String, status: ToDoStatus, completedAt: LocalDateTime?, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET task_repeat = :repeat, task_updatedAt = :updatedAt WHERE task_id = :id")
    abstract suspend fun updateTaskRepeat(id: String, repeat: ToDoRepeat, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoTaskDb SET task_listId = :listId, task_updatedAt = :updatedAt WHERE task_id IN (:ids)")
    abstract suspend fun updateTaskList(ids: List<String>, listId: String, updatedAt: LocalDateTime)

    @Transaction
    open suspend fun rearrangeTask(data: List<ToDoTaskDb>) {
        deleteTask(data)
        insertTask(data)
    }

    @Query("UPDATE ToDoTaskDb SET task_note = :note, task_noteUpdatedAt = :updatedAt,task_updatedAt = :updatedAt WHERE task_id = :id")
    abstract suspend fun updateTaskNote(id: String, note: String, updatedAt: LocalDateTime)

}
