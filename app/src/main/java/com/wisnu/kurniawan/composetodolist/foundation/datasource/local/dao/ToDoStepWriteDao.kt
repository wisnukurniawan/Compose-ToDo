package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import java.time.LocalDateTime

@Dao
abstract class ToDoStepWriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertStep(data: List<ToDoStepDb>)

    @Delete
    abstract suspend fun deleteStep(data: List<ToDoStepDb>)

    @Query("UPDATE ToDoStepDb SET step_name = :name, step_updatedAt = :updatedAt WHERE step_id = :id")
    abstract suspend fun updateStepName(id: String, name: String, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoStepDb SET step_status = :status, step_updatedAt = :updatedAt WHERE step_id = :id")
    abstract suspend fun updateStepStatus(id: String, status: ToDoStatus, updatedAt: LocalDateTime)

    @Query("DELETE FROM ToDoStepDb WHERE step_id = :id")
    abstract suspend fun deleteStepById(id: String)

    @Transaction
    open suspend fun rearrangeStep(data: List<ToDoStepDb>) {
        deleteStep(data)
        insertStep(data)
    }

}
