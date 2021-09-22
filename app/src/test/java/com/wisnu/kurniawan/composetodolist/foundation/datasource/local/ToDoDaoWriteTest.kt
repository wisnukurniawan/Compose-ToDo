package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wisnu.kurniawan.composetodolist.DateFactory
import com.wisnu.kurniawan.composetodolist.expect
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

    private lateinit var toDoWriteDao: ToDoWriteDao
    private lateinit var toDoReadDao: ToDoReadDao
    private lateinit var db: ToDoDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        toDoWriteDao = db.toDoWriteDao()
        toDoReadDao = db.toDoReadDao()
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

        toDoWriteDao.insertGroup(listOf(group1, group2))

        toDoReadDao.getGroup().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertGroup(listOf(group2))

        toDoReadDao.getGroup().expect(
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

        toDoWriteDao.insertGroup(listOf(group1, group2))

        toDoWriteDao.deleteGroup(listOf(group1))

        toDoReadDao.getGroup().expect(
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

        toDoWriteDao.insertGroup(listOf(group1, group2))

        toDoWriteDao.deleteGroup("1")

        toDoReadDao.getGroup().expect(
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

        toDoWriteDao.insertGroup(listOf(groupDefault, group1))
        toDoWriteDao.insertList(listOf(list1, list2))

        toDoWriteDao.ungroup(groupId1, updatedAt, listOf(list1, list2).map { it.id })

        toDoReadDao.getList().expect(
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

        toDoWriteDao.insertGroup(listOf(group1, group2))

        toDoWriteDao.updateGroupName(id = group1.id, name = "new name", updatedAt)

        toDoReadDao.getGroup().expect(
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

        toDoWriteDao.insertGroup(listOf(group1, group2, group3))

        toDoWriteDao.rearrangeGroup(listOf(group3, group2, group1))

        toDoReadDao.getGroup().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))

        toDoWriteDao.insertList(listOf(list1, list2))

        toDoReadDao.getList().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))

        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertList(listOf(list2))

        toDoReadDao.getList().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1, list2))
        toDoWriteDao.insertTask(listOf(task1, task2))
        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoWriteDao.deleteList(listOf(list1))

        toDoReadDao.getList().expect(
            listOf(list2)
        )
        toDoReadDao.getTask().expect(
            listOf(task2)
        )
        toDoReadDao.getStep().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1, list2))

        toDoWriteDao.updateListNameAndColor(id = list1.id, name = "new name", color = ToDoColor.RED, updatedAt = updatedAt)

        toDoReadDao.getList().expect(
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

        toDoWriteDao.insertGroup(listOf(group1, group2))
        toDoWriteDao.insertList(listOf(list1, list2))

        toDoWriteDao.updateListGroup(ids = listOf(list1.id), groupId = groupId2, updatedAt = updatedAt)

        toDoReadDao.getList().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1, list2, list3))

        toDoWriteDao.rearrangeList(listOf(list3, list2, list1))

        toDoReadDao.getList().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))

        toDoWriteDao.insertTask(listOf(task1, task2))

        toDoReadDao.getTask().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1, task2))
        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoWriteDao.deleteTask(listOf(task1))

        toDoReadDao.getTask().expect(
            listOf(task2)
        )
        toDoReadDao.getStep().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1, task2))

        toDoWriteDao.updateTaskName(id = task1.id, name = "new name", updatedAt = updatedAt)

        toDoReadDao.getTask().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1, task2))

        toDoWriteDao.updateTaskStatus(id = task1.id, status = ToDoStatus.COMPLETE, updatedAt = updatedAt, completedAt = null)

        toDoReadDao.getTask().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1, list2))
        toDoWriteDao.insertTask(listOf(task1, task2))

        toDoWriteDao.updateTaskList(ids = listOf(task1.id, task2.id), listId = listId2, updatedAt = updatedAt)

        toDoReadDao.getTask().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1, task2, task3))

        toDoWriteDao.rearrangeTask(listOf(task3, task2, task1))

        toDoReadDao.getTask().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1))

        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoReadDao.getStep().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1))
        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoWriteDao.deleteStep(listOf(step2))

        toDoReadDao.getStep().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1))
        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoWriteDao.updateStepName(id = step1.id, name = "new name", updatedAt = updatedAt)

        toDoReadDao.getStep().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1))
        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoWriteDao.updateStepStatus(id = step1.id, status = ToDoStatus.COMPLETE, updatedAt = updatedAt)

        toDoReadDao.getStep().expect(
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

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1))
        toDoWriteDao.insertStep(listOf(step1, step2, step3))

        toDoWriteDao.rearrangeStep(listOf(step3, step2, step1))

        toDoReadDao.getStep().expect(
            listOf(step3, step2, step1)
        )
    }

}
