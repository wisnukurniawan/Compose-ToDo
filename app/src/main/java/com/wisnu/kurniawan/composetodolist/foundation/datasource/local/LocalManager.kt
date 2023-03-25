package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoGroupReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoListReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoTaskReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.groupWithListToGroup
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toDoListWithTasksToToDoList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toGroupDp
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toGroupIdWithList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toToDoListDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toStepDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toGroup
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toToDoList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toTask
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList
import com.wisnu.kurniawan.composetodolist.model.TaskWithList
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.model.ToDoTaskOverallCount
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

class LocalManager @Inject constructor(
    @Named(DiName.DISPATCHER_IO) private val dispatcher: CoroutineDispatcher,
    private val toDoWriteDao: ToDoWriteDao,
    private val toDoGroupReadDao: ToDoGroupReadDao,
    private val toDoListReadDao: ToDoListReadDao,
    private val toDoTaskReadDao: ToDoTaskReadDao
) {

    fun getGroup(): Flow<List<ToDoGroup>> {
        return toDoGroupReadDao.getGroup()
            .filterNotNull()
            .map { it.toGroup() }
            .flowOn(dispatcher)
    }

    fun getGroup(groupId: String): Flow<ToDoGroup> {
        return toDoGroupReadDao.getGroup(groupId)
            .filterNotNull()
            .map { it.toGroup() }
            .flowOn(dispatcher)
    }

    fun getGroupWithList(): Flow<List<ToDoGroup>> {
        return toDoGroupReadDao.getGroupWithList()
            .filterNotNull()
            .map { it.groupWithListToGroup() }
            .flowOn(dispatcher)
    }

    fun getListWithTasks(): Flow<List<ToDoList>> {
        return toDoListReadDao.getListWithTasks()
            .filterNotNull()
            .map { it.toDoListWithTasksToToDoList() }
            .flowOn(dispatcher)
    }

    fun getList(): Flow<List<ToDoList>> {
        return toDoListReadDao.getList()
            .filterNotNull()
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getOverallCount(date: LocalDateTime): Flow<ToDoTaskOverallCount> {
        return toDoTaskReadDao.getTaskOverallCount(date)
            .flowOn(dispatcher)
    }

    fun getListById(listId: String): Flow<ToDoList> {
        return toDoListReadDao.getListById(listId)
            .filterNotNull()
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getListByGroupId(groupId: String): Flow<List<ToDoList>> {
        return toDoListReadDao.getListByGroupId(groupId)
            .filterNotNull()
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getListWithTasksById(listId: String): Flow<ToDoList> {
        return toDoListReadDao.getListWithTasksById(listId)
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getTaskWithListOrderByDueDate(): Flow<List<TaskWithList>> {
        return toDoTaskReadDao.getTaskWithListOrderByDueDate()
            .filterNotNull()
            .map { tasks ->
                tasks.map {
                    TaskWithList(it.list.toToDoList(), it.task.toTask())
                }
            }
            .flowOn(dispatcher)
    }

    fun getTaskWithListOrderByDueDateToday(date: LocalDateTime): Flow<List<TaskWithList>> {
        return toDoTaskReadDao.getTaskWithListOrderByDueDateToday(date)
            .filterNotNull()
            .map { tasks ->
                tasks.map {
                    TaskWithList(it.list.toToDoList(), it.task.toTask())
                }
            }
            .flowOn(dispatcher)
    }

    fun getTaskWithStepsById(taskId: String): Flow<ToDoTask> {
        return toDoTaskReadDao.getTaskWithStepsById(taskId)
            .filterNotNull()
            .map { it.toTask() }
            .flowOn(dispatcher)
    }

    fun getTaskWithListById(taskId: String): Flow<TaskWithList> {
        return toDoTaskReadDao.getTaskWithListById(taskId)
            .filterNotNull()
            .map { TaskWithList(it.list.toToDoList(), it.task.toTask()) }
            .flowOn(dispatcher)
    }

    fun searchTaskWithList(query: String): Flow<List<TaskWithList>> {
        return toDoTaskReadDao.searchTaskWithList(query)
            .map { tasks ->
                tasks.map {
                    TaskWithList(it.list.toToDoList(), it.task.toTask())
                }
            }
            .flowOn(dispatcher)
    }

    fun getScheduledTasks(): Flow<List<ToDoTask>> {
        return toDoTaskReadDao.getScheduledTasks()
            .map { it.toTask() }
            .flowOn(dispatcher)
    }

    fun getListWithUnGroupList(groupId: String): Flow<List<GroupIdWithList>> {
        return toDoListReadDao.getListWithUnGroupList(groupId)
            .filterNotNull()
            .map { it.toGroupIdWithList() }
            .flowOn(dispatcher)
    }

    suspend fun insertGroup(data: List<ToDoGroup>) {
        withContext(dispatcher) {
            toDoWriteDao.insertGroup(data.toGroupDp())
        }
    }

    suspend fun ungroup(groupId: String, updatedAt: LocalDateTime, listIds: List<String>) {
        withContext(dispatcher) {
            toDoWriteDao.ungroup(groupId, updatedAt, listIds)
        }
    }

    suspend fun insertList(data: List<ToDoList>, groupId: String) {
        withContext(dispatcher) {
            toDoWriteDao.insertList(data.toToDoListDb(groupId))
        }
    }

    suspend fun deleteListById(listId: String) {
        withContext(dispatcher) {
            toDoWriteDao.deleteListById(listId)
        }
    }

    suspend fun updateList(data: List<GroupIdWithList>) {
        withContext(dispatcher) {
            toDoWriteDao.updateList(data.toToDoListDb())
        }
    }

    suspend fun insertTask(data: List<ToDoTask>, listId: String) {
        withContext(dispatcher) {
            toDoWriteDao.insertTask(data.toTaskDb(listId))
        }
    }

    suspend fun insertStep(data: List<ToDoStep>, taskId: String) {
        withContext(dispatcher) {
            toDoWriteDao.insertStep(data.toStepDb(taskId))
        }
    }

    suspend fun updateListNameAndColor(toDoList: ToDoList, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateListNameAndColor(toDoList.id, toDoList.name, toDoList.color, updatedAt)
        }
    }

    suspend fun updateTaskDueDate(
        id: String,
        dueDateTime: LocalDateTime?,
        isDueDateTimeSet: Boolean,
        updatedAt: LocalDateTime
    ) {
        withContext(dispatcher) {
            toDoWriteDao.updateTaskDueDate(id, dueDateTime, isDueDateTimeSet, updatedAt)
        }
    }

    suspend fun resetTaskDueDate(
        id: String,
        updatedAt: LocalDateTime
    ) {
        withContext(dispatcher) {
            toDoWriteDao.resetTaskDueDate(
                id,
                null,
                false,
                ToDoRepeat.NEVER,
                updatedAt
            )
        }
    }

    suspend fun updateTaskRepeat(id: String, repeat: ToDoRepeat, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateTaskRepeat(id, repeat, updatedAt)
        }
    }

    suspend fun updateTaskStatus(id: String, status: ToDoStatus, completedAt: LocalDateTime?, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateTaskStatus(id, status, completedAt, updatedAt)
        }
    }

    suspend fun updateTaskNote(id: String, note: String, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateTaskNote(id, note, updatedAt)
        }
    }

    suspend fun updateStepStatus(id: String, status: ToDoStatus, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateStepStatus(id, status, updatedAt)
        }
    }

    suspend fun updateTaskName(id: String, name: String, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateTaskName(id, name, updatedAt)
        }
    }

    suspend fun updateStepName(id: String, name: String, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateStepName(id, name, updatedAt)
        }
    }

    suspend fun deleteTaskById(id: String) {
        withContext(dispatcher) {
            toDoWriteDao.deleteTaskById(id)
        }
    }

    suspend fun deleteStepById(id: String) {
        withContext(dispatcher) {
            toDoWriteDao.deleteStepById(id)
        }
    }

    suspend fun updateGroupName(id: String, name: String, updatedAt: LocalDateTime) {
        withContext(dispatcher) {
            toDoWriteDao.updateGroupName(id, name, updatedAt)
        }
    }

    suspend fun deleteGroup(id: String) {
        withContext(dispatcher) {
            toDoWriteDao.deleteGroup(id)
        }
    }

}
