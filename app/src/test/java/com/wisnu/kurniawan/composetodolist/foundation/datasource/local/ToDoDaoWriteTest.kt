package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wisnu.kurniawan.composetodolist.DateFactory
import com.wisnu.kurniawan.composetodolist.expect
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoGroupReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoGroupWriteDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoListReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoListWriteDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoStepReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoStepWriteDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoTaskReadDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.dao.ToDoTaskWriteDao
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(RobolectricTestRunner::class)
class ToDoDaoWriteTest {

    private lateinit var toDoGroupWriteDao: ToDoGroupWriteDao
    private lateinit var toDoListWriteDao: ToDoListWriteDao
    private lateinit var toDoTaskWriteDao: ToDoTaskWriteDao
    private lateinit var toDoStepWriteDao: ToDoStepWriteDao
    private lateinit var toDoGroupReadDao: ToDoGroupReadDao
    private lateinit var toDoListReadDao: ToDoListReadDao
    private lateinit var toDoTaskReadDao: ToDoTaskReadDao
    private lateinit var toDoStepReadDao: ToDoStepReadDao
    private lateinit var db: ToDoDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        toDoGroupWriteDao = db.toDoGroupWriteDao()
        toDoListWriteDao = db.toDoListWriteDao()
        toDoTaskWriteDao = db.toDoTaskWriteDao()
        toDoStepWriteDao = db.toDoStepWriteDao()
        toDoGroupReadDao = db.toDoGroupReadDao()
        toDoListReadDao = db.toDoListReadDao()
        toDoTaskReadDao = db.toDoTaskReadDao()
        toDoStepReadDao = db.toDoStepReadDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    // Group section

    @Test
    fun insertGroup() = runBlocking {
        val group1 = ToDoGroupDb(
            id = "1",
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group2 = ToDoGroupDb(
            id = "2",
            name = "group2",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1, group2))

        toDoGroupReadDao.getGroup().expect(
            listOf(group1, group2)
        )
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertDuplicateNameGroup() = runBlocking {
        val group1 = ToDoGroupDb(
            id = "1",
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group2 = ToDoGroupDb(
            id = "2",
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoGroupWriteDao.insertGroup(listOf(group2))

        toDoGroupReadDao.getGroup().expect(
            listOf(group1)
        )
    }

    @Test
    fun deleteGroup() = runBlocking {
        val group1 = ToDoGroupDb(
            id = "1",
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group2 = ToDoGroupDb(
            id = "2",
            name = "group2",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1, group2))

        toDoGroupWriteDao.deleteGroup(listOf(group1))

        toDoGroupReadDao.getGroup().expect(
            listOf(group2)
        )
    }

    @Test
    fun deleteGroupById() = runBlocking {
        val group1 = ToDoGroupDb(
            id = "1",
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group2 = ToDoGroupDb(
            id = "2",
            name = "group2",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1, group2))

        toDoGroupWriteDao.deleteGroup("1")

        toDoGroupReadDao.getGroup().expect(
            listOf(group2)
        )
    }

    @Test
    fun ungroup() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val groupId1 = "group1"
        val listId1 = "listId1"
        val listId2 = "listId2"
        val groupDefault = ToDoGroupDb(
            id = ToDoGroupDb.DEFAULT_ID,
            name = "groupDefault",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group1 = ToDoGroupDb(
            id = groupId1,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = groupId1,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId2,
            name = "list2",
            groupId = groupId1,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(groupDefault, group1))
        toDoListWriteDao.insertList(listOf(list1, list2))

        toDoListWriteDao.updateListGroup(listOf(list1, list2).map { it.id }, groupId1, updatedAt)
        toDoGroupWriteDao.deleteGroup(groupId1)

        toDoListReadDao.getList().expect(
            listOf(
                list1.copy(groupId = ToDoGroupDb.DEFAULT_ID, updatedAt = updatedAt),
                list2.copy(groupId = ToDoGroupDb.DEFAULT_ID, updatedAt = updatedAt)
            )
        )
    }

    @Test
    fun updateGroupName() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val group1 = ToDoGroupDb(
            id = "1",
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group2 = ToDoGroupDb(
            id = "2",
            name = "group2",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1, group2))

        toDoGroupWriteDao.updateGroupName(id = group1.id, name = "new name", updatedAt)

        toDoGroupReadDao.getGroup().expect(
            listOf(
                group1.copy(name = "new name", updatedAt = updatedAt),
                group2
            ),
        )
    }

    @Test
    fun rearrangeGroup() = runBlocking {
        val group1 = ToDoGroupDb(
            id = "1",
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group2 = ToDoGroupDb(
            id = "2",
            name = "group2",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group3 = ToDoGroupDb(
            id = "3",
            name = "group3",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1, group2, group3))

        toDoGroupWriteDao.rearrangeGroup(listOf(group3, group2, group1))

        toDoGroupReadDao.getGroup().expect(
            listOf(group3, group2, group1)
        )
    }

    // List section

    @Test
    fun insertList() = runBlocking {
        val unknownGroupId = "unknown"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "1",
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "2",
            name = "list2",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))

        toDoListWriteDao.insertList(listOf(list1, list2))

        toDoListReadDao.getList().expect(
            listOf(list1, list2)
        )
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertDuplicateNameList() = runBlocking {
        val unknownGroupId = "unknown"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "1",
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "2",
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))

        toDoListWriteDao.insertList(listOf(list1))
        toDoListWriteDao.insertList(listOf(list2))

        toDoListReadDao.getList().expect(
            listOf(list1)
        )
    }

    @Test
    fun deleteList() = runBlocking {
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val listId2 = "listId2"
        val taskId1 = "taskId1"
        val taskId2 = "taskId2"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId2,
            name = "list2",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = taskId1,
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task2 = ToDoTaskDb(
            id = taskId2,
            name = "task2",
            listId = listId2,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step1 = ToDoStepDb(
            id = "1",
            name = "step1",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step2 = ToDoStepDb(
            id = "2",
            name = "step2",
            taskId = taskId2,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1, list2))
        toDoTaskWriteDao.insertTask(listOf(task1, task2))
        toDoStepWriteDao.insertStep(listOf(step1, step2))

        toDoListWriteDao.deleteList(listOf(list1))

        toDoListReadDao.getList().expect(
            listOf(list2)
        )
        toDoTaskReadDao.getTask().expect(
            listOf(task2)
        )
        toDoStepReadDao.getStep().expect(
            listOf(step2)
        )
    }

    @Test
    fun updateListName() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val unknownGroupId = "unknown"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "1",
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "2",
            name = "list2",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1, list2))

        toDoListWriteDao.updateListNameAndColor(id = list1.id, name = "new name", color = ToDoColor.RED, updatedAt = updatedAt)

        toDoListReadDao.getList().expect(
            listOf(
                list1.copy(name = "new name", color = ToDoColor.RED, updatedAt = updatedAt),
                list2
            )
        )
    }

    @Test
    fun updateListGroup() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val unknownGroupId = "unknown"
        val groupId2 = "groupId2"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group2 = ToDoGroupDb(
            id = groupId2,
            name = "group2",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "1",
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "2",
            name = "list2",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1, group2))
        toDoListWriteDao.insertList(listOf(list1, list2))

        toDoListWriteDao.updateListGroup(ids = listOf(list1.id), groupId = groupId2, updatedAt = updatedAt)

        toDoListReadDao.getList().expect(
            listOf(
                list1.copy(groupId = groupId2, updatedAt = updatedAt),
                list2
            )
        )
    }

    @Test
    fun rearrangeList() = runBlocking {
        val unknownGroupId = "unknown"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "1",
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "2",
            name = "list2",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list3 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = "3",
            name = "list3",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1, list2, list3))

        toDoListWriteDao.rearrangeList(listOf(list3, list2, list1))

        toDoListReadDao.getList().expect(
            listOf(list3, list2, list1)
        )
    }

    // Task section

    @Test
    fun insertTask() = runBlocking {
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = "1",
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task2 = ToDoTaskDb(
            id = "2",
            name = "task2",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))

        toDoTaskWriteDao.insertTask(listOf(task1, task2))

        toDoTaskReadDao.getTask().expect(
            listOf(task1, task2)
        )
    }

    @Test
    fun deleteTask() = runBlocking {
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val taskId1 = "taskId1"
        val taskId2 = "taskId2"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = taskId1,
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task2 = ToDoTaskDb(
            id = taskId2,
            name = "task2",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step1 = ToDoStepDb(
            id = "1",
            name = "step1",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step2 = ToDoStepDb(
            id = "2",
            name = "step2",
            taskId = taskId2,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1, task2))
        toDoStepWriteDao.insertStep(listOf(step1, step2))

        toDoTaskWriteDao.deleteTask(listOf(task1))

        toDoTaskReadDao.getTask().expect(
            listOf(task2)
        )
        toDoStepReadDao.getStep().expect(
            listOf(step2)
        )
    }

    @Test
    fun updateTaskName() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = "1",
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task2 = ToDoTaskDb(
            id = "2",
            name = "task2",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1, task2))

        toDoTaskWriteDao.updateTaskName(id = task1.id, name = "new name", updatedAt = updatedAt)

        toDoTaskReadDao.getTask().expect(
            listOf(
                task1.copy(name = "new name", updatedAt = updatedAt),
                task2
            )
        )
    }

    @Test
    fun updateTaskStatus() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = "1",
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task2 = ToDoTaskDb(
            id = "2",
            name = "task2",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1, task2))

        toDoTaskWriteDao.updateTaskStatus(id = task1.id, status = ToDoStatus.COMPLETE, updatedAt = updatedAt, completedAt = null)

        toDoTaskReadDao.getTask().expect(
            listOf(
                task1.copy(status = ToDoStatus.COMPLETE, updatedAt = updatedAt),
                task2
            )
        )
    }

    @Test
    fun updateTaskList() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val listId2 = "listId2"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list2 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId2,
            name = "list2",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = "1",
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task2 = ToDoTaskDb(
            id = "2",
            name = "task2",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1, list2))
        toDoTaskWriteDao.insertTask(listOf(task1, task2))

        toDoTaskWriteDao.updateTaskList(ids = listOf(task1.id, task2.id), listId = listId2, updatedAt = updatedAt)

        toDoTaskReadDao.getTask().expect(
            listOf(
                task1.copy(listId = listId2, updatedAt = updatedAt),
                task2.copy(listId = listId2, updatedAt = updatedAt)
            )
        )
    }

    @Test
    fun rearrangeTask() = runBlocking {
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = "1",
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task2 = ToDoTaskDb(
            id = "2",
            name = "task2",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task3 = ToDoTaskDb(
            id = "3",
            name = "task3",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1, task2, task3))

        toDoTaskWriteDao.rearrangeTask(listOf(task3, task2, task1))

        toDoTaskReadDao.getTask().expect(
            listOf(task3, task2, task1)
        )
    }

    // Step section

    @Test
    fun insertStep() = runBlocking {
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val taskId1 = "taskId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = taskId1,
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step1 = ToDoStepDb(
            id = "1",
            name = "step1",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step2 = ToDoStepDb(
            id = "2",
            name = "step2",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1))

        toDoStepWriteDao.insertStep(listOf(step1, step2))

        toDoStepReadDao.getStep().expect(
            listOf(step1, step2)
        )
    }

    @Test
    fun deleteStep() = runBlocking {
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val taskId1 = "taskId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = taskId1,
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step1 = ToDoStepDb(
            id = "1",
            name = "step1",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step2 = ToDoStepDb(
            id = "2",
            name = "step2",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1))
        toDoStepWriteDao.insertStep(listOf(step1, step2))

        toDoStepWriteDao.deleteStep(listOf(step2))

        toDoStepReadDao.getStep().expect(
            listOf(step1)
        )
    }

    @Test
    fun updateStepName() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val taskId1 = "taskId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = taskId1,
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step1 = ToDoStepDb(
            id = "1",
            name = "step1",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step2 = ToDoStepDb(
            id = "2",
            name = "step2",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1))
        toDoStepWriteDao.insertStep(listOf(step1, step2))

        toDoStepWriteDao.updateStepName(id = step1.id, name = "new name", updatedAt = updatedAt)

        toDoStepReadDao.getStep().expect(
            listOf(
                step1.copy(name = "new name", updatedAt = updatedAt),
                step2
            )
        )
    }

    @Test
    fun updateStepStatus() = runBlocking {
        val updatedAt = LocalDateTime.of(2021, 1, 2, 0, 0, 0, 0)
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val taskId1 = "taskId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = taskId1,
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step1 = ToDoStepDb(
            id = "1",
            name = "step1",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step2 = ToDoStepDb(
            id = "2",
            name = "step2",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1))
        toDoStepWriteDao.insertStep(listOf(step1, step2))

        toDoStepWriteDao.updateStepStatus(id = step1.id, status = ToDoStatus.COMPLETE, updatedAt = updatedAt)

        toDoStepReadDao.getStep().expect(
            listOf(
                step1.copy(status = ToDoStatus.COMPLETE, updatedAt = updatedAt),
                step2
            )
        )
    }

    @Test
    fun rearrangeStep() = runBlocking {
        val unknownGroupId = "unknown"
        val listId1 = "listId1"
        val taskId1 = "taskId1"
        val group1 = ToDoGroupDb(
            id = unknownGroupId,
            name = "group1",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list1 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId1,
            name = "list1",
            groupId = unknownGroupId,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val task1 = ToDoTaskDb(
            id = taskId1,
            name = "task1",
            listId = listId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step1 = ToDoStepDb(
            id = "1",
            name = "step1",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step2 = ToDoStepDb(
            id = "2",
            name = "step2",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val step3 = ToDoStepDb(
            id = "3",
            name = "step2",
            taskId = taskId1,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoGroupWriteDao.insertGroup(listOf(group1))
        toDoListWriteDao.insertList(listOf(list1))
        toDoTaskWriteDao.insertTask(listOf(task1))
        toDoStepWriteDao.insertStep(listOf(step1, step2, step3))

        toDoStepWriteDao.rearrangeStep(listOf(step3, step2, step1))

        toDoStepReadDao.getStep().expect(
            listOf(step3, step2, step1)
        )
    }

}
