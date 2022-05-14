package com.wisnu.kurniawan.composetodolist.features.todo.detail.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wisnu.kurniawan.composetodolist.BaseViewModelTest
import com.wisnu.kurniawan.composetodolist.DateFactory
import com.wisnu.kurniawan.composetodolist.features.todo.detail.data.IListDetailEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.extension.update
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListPurple
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProviderImpl
import com.wisnu.kurniawan.composetodolist.model.ToDoColor
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_LIST_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalCoroutinesApi
class UpdateListDetailViewModelTest : BaseViewModelTest() {

    @Test
    fun init() = runTest {
        val environment = buildEnvironment(
            ToDoList(
                id = "id-123",
                name = "name-123",
                color = ToDoColor.PURPLE,
                tasks = listOf(),
                createdAt = DateFactory.constantDate,
                updatedAt = DateFactory.constantDate,
            )
        )
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.set(ARG_LIST_ID, "id-123")

        val viewModel = ListDetailViewModel(
            savedStateHandle,
            environment
        )

        viewModel.state.test {
            Assert.assertEquals(
                ListDetailState(
                    list = ToDoList(
                        id = "id-123",
                        name = "name-123",
                        color = ToDoColor.PURPLE,
                        tasks = listOf(),
                        createdAt = DateFactory.constantDate,
                        updatedAt = DateFactory.constantDate,
                    ),
                    newListName = "name-123",
                    colors = viewModel.state.value.colors.update(ListPurple)
                ),
                awaitItem()
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun buildEnvironment(returnedListWithTasks: ToDoList) = object : IListDetailEnvironment {
        override val idProvider: IdProvider = IdProviderImpl()
        override val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()
        override fun getListWithTasksById(listId: String): Flow<ToDoList> {
            return flow {
                emit(returnedListWithTasks)
            }
        }

        override suspend fun createList(toDoList: ToDoList): Flow<ToDoList> {
            return flow {}
        }

        override suspend fun updateList(toDoList: ToDoList): Flow<Any> {
            return flow { emit(Any()) }
        }

        override suspend fun createTask(taskName: String, listId: String) {

        }

        override suspend fun toggleTaskStatus(toDoTask: ToDoTask) {

        }

        override suspend fun deleteTask(toDoTask: ToDoTask) {

        }
    }

}
