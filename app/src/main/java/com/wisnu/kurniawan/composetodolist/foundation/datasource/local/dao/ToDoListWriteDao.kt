package com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import java.time.LocalDateTime

@Dao
interface ToDoListWriteDao {

    @Insert
    suspend fun insertList(data: List<ToDoListDb>)

    @Delete
    suspend fun deleteList(data: List<ToDoListDb>)

    @Query("DELETE FROM ToDoListDb WHERE list_id = :id")
    suspend fun deleteListById(id: String)

    @Update
    suspend fun updateList(data: List<ToDoListDb>)

    @Query("UPDATE ToDoListDb SET list_name = :name, list_color = :color, list_updatedAt = :updatedAt WHERE list_id = :id")
    suspend fun updateListNameAndColor(id: String, name: String, color: ToDoColor, updatedAt: LocalDateTime)

    @Query("UPDATE ToDoListDb SET list_groupId = :groupId, list_updatedAt = :updatedAt WHERE list_id IN (:ids)")
    suspend fun updateListGroup(ids: List<String>, groupId: String, updatedAt: LocalDateTime)

//    @Transaction
//    suspend fun rearrangeList(data: List<ToDoListDb>) {
//        deleteList(data)
//        insertList(data)
//    }

}
