package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import java.time.LocalDateTime

/**
 * The omnibus create/update/delete, also known as last-write-wins.
 * Set the resource to the provided value.
 * If the provided value is null, delete it.
 * If two differing set operations arrive concurrently, pick a “latest” to win. Optionally, also return its previous value.
 */
@Dao
interface ToDoGroupWriteDao {

    @Insert
    suspend fun insertGroup(data: List<ToDoGroupDb>)

    @Delete
    suspend fun deleteGroup(data: List<ToDoGroupDb>)

    @Query("DELETE FROM ToDoGroupDb WHERE group_id = :id")
    suspend fun deleteGroup(id: String)

    @Query("UPDATE ToDoGroupDb SET group_name = :name, group_updatedAt = :updatedAt WHERE group_id = :id")
    suspend fun updateGroupName(id: String, name: String, updatedAt: LocalDateTime)

//    @Transaction
//    suspend fun rearrangeGroup(data: List<ToDoGroupDb>) {
//        deleteGroup(data)
//        insertGroup(data)
//    }

}
