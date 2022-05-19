package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.toDoGroupWithListToGroup
import com.wisnu.kurniawan.composetodolist.foundation.extension.toDoListWithTasksToToDoList
import com.wisnu.kurniawan.composetodolist.foundation.extension.toGroupDp
import com.wisnu.kurniawan.composetodolist.foundation.extension.toGroupIdWithList
import com.wisnu.kurniawan.composetodolist.foundation.extension.toListDb
import com.wisnu.kurniawan.composetodolist.foundation.extension.toStepDb
import com.wisnu.kurniawan.composetodolist.foundation.extension.toTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.extension.toToDoGroup
import com.wisnu.kurniawan.composetodolist.foundation.extension.toToDoList
import com.wisnu.kurniawan.composetodolist.foundation.extension.toToDoTask
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
    private val toDoReadDao: ToDoReadDao,
    private val toDoWriteDao: ToDoWriteDao
) {

    fun getGroup(): Flow<List<ToDoGroup>> {
        return toDoReadDao.getGroup()
            .filterNotNull()
            .map { it.toToDoGroup() }
            .flowOn(dispatcher)
    }

    fun getGroup(groupId: String): Flow<ToDoGroup> {
        return toDoReadDao.getGroup(groupId)
            .filterNotNull()
            .map { it.toToDoGroup() }
            .flowOn(dispatcher)
    }

    fun getGroupWithList(): Flow<List<ToDoGroup>> {
        return toDoReadDao.getGroupWithList()
            .filterNotNull()
            .map { it.toDoGroupWithListToGroup() }
            .flowOn(dispatcher)
    }

    fun getListWithTasks(): Flow<List<ToDoList>> {
        return toDoReadDao.getListWithTasks()
            .filterNotNull()
            .map { it.toDoListWithTasksToToDoList() }
            .flowOn(dispatcher)
    }

    fun getList(): Flow<List<ToDoList>> {
        return toDoReadDao.getList()
            .filterNotNull()
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getOverallCount(date: LocalDateTime): Flow<ToDoTaskOverallCount> {
        return toDoReadDao.getTaskOverallCount(date)
            .flowOn(dispatcher)
    }

    fun getListById(listId: String): Flow<ToDoList> {
        return toDoReadDao.getListById(listId)
            .filterNotNull()
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getListByGroupId(groupId: String): Flow<List<ToDoList>> {
        return toDoReadDao.getListByGroupId(groupId)
            .filterNotNull()
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getListWithTasksById(listId: String): Flow<ToDoList> {
        return toDoReadDao.getListWithTasksById(listId)
            .map { it.toToDoList() }
            .flowOn(dispatcher)
    }

    fun getTaskWithListOrderByDueDate(): Flow<List<TaskWithList>> {
        return toDoReadDao.getTaskWithListOrderByDueDate()
            .filterNotNull()
            .map { tasks ->
                tasks.map {
                    TaskWithList(it.list.toToDoList(), it.task.toToDoTask())
                }
            }
            .flowOn(dispatcher)
    }

    fun getTaskWithListOrderByDueDateToday(date: LocalDateTime): Flow<List<TaskWithList>> {
        return toDoReadDao.getTaskWithListOrderByDueDateToday(date)
            .filterNotNull()
            .map { tasks ->
                tasks.map {
                    TaskWithList(it.list.toToDoList(), it.task.toToDoTask())
                }
            }
            .flowOn(dispatcher)
    }

    fun getTaskWithStepsById(taskId: String): Flow<ToDoTask> {
        return toDoReadDao.getTaskWithStepsById(taskId)
            .filterNotNull()
            .map { it.toToDoTask() }
            .flowOn(dispatcher)
    }

    fun getTaskWithListById(taskId: String): Flow<TaskWithList> {
        return toDoReadDao.getTaskWithListById(taskId)
            .filterNotNull()
            .map { TaskWithList(it.list.toToDoList(), it.task.toToDoTask()) }
            .flowOn(dispatcher)
    }

    fun searchTaskWithList(query: String): Flow<List<TaskWithList>> {
        return toDoReadDao.searchTaskWithList(query)
            .map { tasks ->
                tasks.map {
                    TaskWithList(it.list.toToDoList(), it.task.toToDoTask())
                }
            }
            .flowOn(dispatcher)
    }

    fun getScheduledTasks(): Flow<List<ToDoTask>> {
        return toDoReadDao.getScheduledTasks()
            .map { it.toToDoTask() }
            .flowOn(dispatcher)
    }

    fun getListWithUnGroupList(groupId: String): Flow<List<GroupIdWithList>> {
        return toDoReadDao.getListWithUnGroupList(groupId)
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
            toDoWriteDao.insertList(data.toListDb(groupId))
        }
    }

    suspend fun deleteListById(listId: String) {
        withContext(dispatcher) {
            toDoWriteDao.deleteListById(listId)
        }
    }

    suspend fun updateList(data: List<GroupIdWithList>) {
        withContext(dispatcher) {
            toDoWriteDao.updateList(data.toListDb())
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
