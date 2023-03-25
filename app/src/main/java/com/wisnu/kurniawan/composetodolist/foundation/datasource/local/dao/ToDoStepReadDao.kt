package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoStepReadDao {

    @Query("SELECT * FROM ToDoStepDb")
    fun getStep(): Flow<List<ToDoStepDb>>

    @Query("SELECT * FROM ToDoStepDb WHERE step_taskId = :taskId")
    fun getStep(taskId: String): Flow<List<ToDoStepDb>>

}
