package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.DateFactory
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.mapper.toStep
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoStepDb
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoStep
import org.junit.Assert
import org.junit.Test

class ToDoStepDbExtTest {

    @Test
    fun toStepDb() {
        Assert.assertEquals(
            listOf(
                ToDoStep(
                    id = "id",
                    name = "name",
                    status = ToDoStatus.IN_PROGRESS,
                    createdAt = DateFactory.constantDate,
                    updatedAt = DateFactory.constantDate,
                )
            ),
            listOf(
                ToDoStepDb(
                    id = "id",
                    name = "name",
                    status = ToDoStatus.IN_PROGRESS,
                    taskId = "taskId",
                    createdAt = DateFactory.constantDate,
                    updatedAt = DateFactory.constantDate,
                )
            ).toStep()
        )
    }
}
