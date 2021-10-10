package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import com.wisnu.kurniawan.composetodolist.foundation.extension.groupDbToGroup
import com.wisnu.kurniawan.composetodolist.foundation.extension.toDoGroupWithListToGroup
import com.wisnu.kurniawan.composetodolist.foundation.extension.toDoListWithTasksToList
import com.wisnu.kurniawan.composetodolist.foundation.extension.toGroupDp
import com.wisnu.kurniawan.composetodolist.foundation.extension.toGroupIdWithList
import com.wisnu.kurniawan.composetodolist.foundation.extension.toList
import com.wisnu.kurniawan.composetodolist.foundation.extension.toListDb
import com.wisnu.kurniawan.composetodolist.foundation.extension.toStepDb
import com.wisnu.kurniawan.composetodolist.foundation.extension.toTask
import com.wisnu.kurniawan.composetodolist.foundation.extension.toTaskDb
import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.model.ToDoTaskOverallCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class LocalManager @Inject constructor(
    private val toDoReadDao: ToDoReadDao,
    private val toDoWriteDao: ToDoWriteDao
) {

    fun getGroup(): Flow<List<ToDoGroup>> {
        return toDoReadDao.getGroup()
            .filterNotNull()
            .map { it.groupDbToGroup() }
    }

    fun getGroup(groupId: String): Flow<ToDoGroup> {
        return toDoReadDao.getGroup(groupId)
            .filterNotNull()
            .map { it.groupDbToGroup() }
    }

    fun getGroupWithList(): Flow<List<ToDoGroup>> {
        return toDoReadDao.getGroupWithList()
            .filterNotNull()
            .map { it.toDoGroupWithListToGroup() }
    }

    fun getList(): Flow<List<ToDoList>> {
        return toDoReadDao.getList()
            .filterNotNull()
            .map { it.toList() }
    }

    fun getOverallCount(date: LocalDateTime): Flow<ToDoTaskOverallCount> {
        return toDoReadDao.getTaskOverallCount(date)
    }

    fun getListById(listId: String): Flow<ToDoList> {
        return toDoReadDao.getListById(listId)
            .filterNotNull()
            .map { it.toList() }
    }

    fun getListByGroupId(groupId: String): Flow<List<ToDoList>> {
        return toDoReadDao.getListByGroupId(groupId)
            .filterNotNull()
            .map { it.toList() }
    }

    fun getListWithTasksById(listId: String): Flow<ToDoList> {
        return toDoReadDao.getListWithTasksById(listId)
            .map { it.toDoListWithTasksToList() }
    }

    fun getToDoTaskWithStepsOrderByDueDateWithListId(): Flow<List<Pair<ToDoTask, String>>> {
        return toDoReadDao.getToDoTaskWithStepsOrderByDueDate()
            .map { tasks ->
                tasks.map {
                    Pair(it.toTask(), it.task.listId)
                }
            }
    }

    fun getTaskWithStepsById(taskId: String): Flow<ToDoTask> {
        return toDoReadDao.getTaskWithStepsById(taskId)
            .filterNotNull()
            .map { it.toTask() }
    }

    fun getTaskWithStepsByIdWithListId(taskId: String): Flow<Pair<ToDoTask, String>> {
        return toDoReadDao.getTaskWithStepsById(taskId)
            .filterNotNull()
            .map { Pair(it.toTask(), it.task.listId) }
    }

    fun getScheduledTasks(): Flow<List<ToDoTask>> {
        return toDoReadDao.getScheduledTasks()
            .map { it.toTask() }
    }

    fun getListWithUnGroupList(groupId: String): Flow<List<GroupIdWithList>> {
        return toDoReadDao.getListWithUnGroupList(groupId)
            .filterNotNull()
            .map { it.toGroupIdWithList() }
    }

    suspend fun insertGroup(data: List<ToDoGroup>) {
        toDoWriteDao.insertGroup(data.toGroupDp())
    }

    suspend fun ungroup(groupId: String, updatedAt: LocalDateTime, listIds: List<String>) {
        toDoWriteDao.ungroup(groupId, updatedAt, listIds)
    }

    suspend fun insertList(data: List<ToDoList>, groupId: String) {
        toDoWriteDao.insertList(data.toListDb(groupId))
    }

    suspend fun deleteListById(listId: String) {
        toDoWriteDao.deleteListById(listId)
    }

    suspend fun updateList(data: List<GroupIdWithList>) {
        toDoWriteDao.updateList(data.toListDb())
    }

    suspend fun insertTask(data: List<ToDoTask>, listId: String) {
        toDoWriteDao.insertTask(data.toTaskDb(listId))
    }

    suspend fun insertStep(data: List<ToDoStep>, taskId: String) {
        toDoWriteDao.insertStep(data.toStepDb(taskId))
    }

    suspend fun updateListNameAndColor(toDoList: ToDoList, updatedAt: LocalDateTime) {
        toDoWriteDao.updateListNameAndColor(toDoList.id, toDoList.name, toDoList.color, updatedAt)
    }

    suspend fun updateTaskDueDate(
        id: String,
        dueDateTime: LocalDateTime?,
        isDueDateTimeSet: Boolean,
        updatedAt: LocalDateTime
    ) {
        toDoWriteDao.updateTaskDueDate(id, dueDateTime, isDueDateTimeSet, updatedAt)
    }

    suspend fun resetTaskDueDate(
        id: String,
        updatedAt: LocalDateTime
    ) {
        toDoWriteDao.resetTaskDueDate(
            id,
            null,
            false,
            ToDoRepeat.NEVER,
            updatedAt
        )
    }

    suspend fun updateTaskRepeat(id: String, repeat: ToDoRepeat, updatedAt: LocalDateTime) {
        toDoWriteDao.updateTaskRepeat(id, repeat, updatedAt)
    }

    suspend fun updateTaskStatus(id: String, status: ToDoStatus, completedAt: LocalDateTime?, updatedAt: LocalDateTime) {
        toDoWriteDao.updateTaskStatus(id, status, completedAt, updatedAt)
    }

    suspend fun updateStepStatus(id: String, status: ToDoStatus, updatedAt: LocalDateTime) {
        toDoWriteDao.updateStepStatus(id, status, updatedAt)
    }

    suspend fun updateTaskName(id: String, name: String, updatedAt: LocalDateTime) {
        toDoWriteDao.updateTaskName(id, name, updatedAt)
    }

    suspend fun updateStepName(id: String, name: String, updatedAt: LocalDateTime) {
        toDoWriteDao.updateStepName(id, name, updatedAt)
    }

    suspend fun deleteTaskById(id: String) {
        toDoWriteDao.deleteTaskById(id)
    }

    suspend fun deleteStepById(id: String) {
        toDoWriteDao.deleteStepById(id)
    }

    suspend fun updateGroupName(id: String, name: String, updatedAt: LocalDateTime) {
        toDoWriteDao.updateGroupName(id, name, updatedAt)
    }

    suspend fun deleteGroup(id: String) {
        toDoWriteDao.deleteGroup(id)
    }

}
