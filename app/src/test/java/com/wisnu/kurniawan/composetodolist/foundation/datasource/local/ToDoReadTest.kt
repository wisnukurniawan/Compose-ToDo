package com.wisnu.kurniawan.composetodolist.foundation.datasource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.wisnu.kurniawan.composetodolist.DateFactory
import com.wisnu.kurniawan.composetodolist.expect
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupWithList
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoListWithTasks
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskDb
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoTaskWithSteps
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(RobolectricTestRunner::class)
class ToDoReadTest {

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

    @Test
    fun getListByTaskId() = runBlocking {
        val groupId1 = "groupId1"
        val groupId2 = "groupId2"
        val listId1 = "listId1"
        val listId2 = "listId2"
        val group1 = ToDoGroupDb(
            id = groupId1,
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
            groupId = groupId2,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoWriteDao.insertGroup(listOf(group1, group2))
        toDoWriteDao.insertList(listOf(list1, list2))

        toDoReadDao.getListByGroupId(groupId1).expect(listOf(list1))
    }

    @Test
    fun getListWithUnGroupList() = runBlocking {
        val groupId1 = "groupId1"
        val groupId2 = "groupId2"
        val listId1 = "listId1"
        val listId2 = "listId2"
        val listId3 = "listId3"
        val listId4 = "listId4"
        val groupDefault = ToDoGroupDb(
            id = ToDoGroupDb.DEFAULT_ID,
            name = "others",
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val group1 = ToDoGroupDb(
            id = groupId1,
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
            groupId = groupId2,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list3 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId3,
            name = "list3",
            groupId = ToDoGroupDb.DEFAULT_ID,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )
        val list4 = ToDoListDb(
            color = ToDoColor.BLUE,
            id = listId4,
            name = "list4",
            groupId = ToDoGroupDb.DEFAULT_ID,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoWriteDao.insertGroup(listOf(groupDefault, group1, group2))
        toDoWriteDao.insertList(listOf(list1, list2, list3, list4))

        toDoReadDao.getListWithUnGroupList(groupId1).expect(listOf(list3, list4, list1))
        toDoReadDao.getListWithUnGroupList("unknown").expect(listOf(list3, list4))
    }

    @Test
    fun getStepByTaskId() = runBlocking {
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
        val step3 = ToDoStepDb(
            id = "3",
            name = "step3",
            taskId = taskId2,
            status = ToDoStatus.IN_PROGRESS,
            createdAt = DateFactory.constantDate,
            updatedAt = DateFactory.constantDate,
        )

        toDoWriteDao.insertGroup(listOf(group1))
        toDoWriteDao.insertList(listOf(list1))
        toDoWriteDao.insertTask(listOf(task1, task2))
        toDoWriteDao.insertStep(listOf(step1, step2, step3))

        toDoReadDao.getStep(taskId2).expect(listOf(step2, step3))
    }

    @Test
    fun getAssociateByDeletingTask() = runBlocking {
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

        toDoReadDao.getGroupWithList().expect(
            listOf(
                ToDoGroupWithList(
                    group = group1,
                    listWithTasks = listOf(
                        ToDoListWithTasks(
                            list = list1,
                            taskWithSteps = listOf(
                                ToDoTaskWithSteps(
                                    task = task2,
                                    steps = listOf(step2)
                                )
                            )
                        )
                    )
                )
            )
        )
        toDoReadDao.getListWithTasks().expect(
            listOf(
                ToDoListWithTasks(
                    list = list1,
                    taskWithSteps = listOf(
                        ToDoTaskWithSteps(
                            task = task2,
                            steps = listOf(step2)
                        )
                    )
                )
            )
        )
        toDoReadDao.getTaskWithSteps().expect(
            listOf(
                ToDoTaskWithSteps(
                    task = task2,
                    steps = listOf(step2)
                )
            )
        )
    }

    @Test
    fun getAssociateByDeletingList() = runBlocking {
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

        toDoReadDao.getGroupWithList().expect(
            listOf(
                ToDoGroupWithList(
                    group = group1,
                    listWithTasks = listOf(
                        ToDoListWithTasks(
                            list = list2,
                            taskWithSteps = listOf(
                                ToDoTaskWithSteps(
                                    task = task2,
                                    steps = listOf(step2)
                                )
                            )
                        )
                    )
                )
            )
        )
        toDoReadDao.getListWithTasks().expect(
            listOf(
                ToDoListWithTasks(
                    list = list2,
                    taskWithSteps = listOf(
                        ToDoTaskWithSteps(
                            task = task2,
                            steps = listOf(step2)
                        )
                    )
                )
            )
        )
        toDoReadDao.getTaskWithSteps().expect(
            listOf(
                ToDoTaskWithSteps(
                    task = task2,
                    steps = listOf(step2)
                )
            )
        )
    }

    @Test
    fun getListWithTasksByOrderId() = runBlocking {
        val groupId1 = "groupId1"
        val groupId2 = "groupId2"
        val listId1 = "listId1"
        val listId2 = "listId2"
        val taskId1 = "taskId1"
        val taskId2 = "taskId2"
        val group1 = ToDoGroupDb(
            id = groupId1,
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
            groupId = groupId2,
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

        toDoWriteDao.insertGroup(listOf(group1, group2))
        toDoWriteDao.insertList(listOf(list1, list2))
        toDoWriteDao.insertTask(listOf(task1, task2))
        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoReadDao.getListWithTasks(groupId1).expect(
            listOf(
                ToDoListWithTasks(
                    list = list1,
                    taskWithSteps = listOf(
                        ToDoTaskWithSteps(
                            task = task1,
                            steps = listOf(step1)
                        )
                    )
                )
            )
        )
    }

    @Test
    fun getTaskWithStepsByListId() = runBlocking {
        val groupId1 = "groupId1"
        val groupId2 = "groupId2"
        val listId1 = "listId1"
        val listId2 = "listId2"
        val taskId1 = "taskId1"
        val taskId2 = "taskId2"
        val group1 = ToDoGroupDb(
            id = groupId1,
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
            groupId = groupId2,
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

        toDoWriteDao.insertGroup(listOf(group1, group2))
        toDoWriteDao.insertList(listOf(list1, list2))
        toDoWriteDao.insertTask(listOf(task1, task2))
        toDoWriteDao.insertStep(listOf(step1, step2))

        toDoReadDao.getTaskWithSteps(listId1).expect(
            listOf(
                ToDoTaskWithSteps(
                    task = task1,
                    steps = listOf(step1)
                )
            )
        )
    }

}
