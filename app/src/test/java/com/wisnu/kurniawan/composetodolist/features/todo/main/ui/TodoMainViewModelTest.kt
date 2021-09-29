package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import app.cash.turbine.test
import com.wisnu.kurniawan.composetodolist.BaseViewModelTest
import com.wisnu.kurniawan.composetodolist.DateFactory
import com.wisnu.kurniawan.composetodolist.features.todo.main.data.IToDoMainEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProviderImpl
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class ToDoMainViewModelTest : BaseViewModelTest() {

    @Test
    fun init() = test {
        val todoMainEnvironment = object : IToDoMainEnvironment {
            override val dispatcher: CoroutineDispatcher = coroutineDispatcher
            override val dateTimeProvider: DateTimeProvider = DateTimeProviderImpl()

            override fun getGroup(): Flow<List<ToDoGroup>> {
                return flow {
                    emit(
                        listOf(
                            ToDoGroup(
                                id = "id",
                                name = "name",
                                lists = listOf(),
                                createdAt = DateFactory.constantDate,
                                updatedAt = DateFactory.constantDate,
                            )
                        )
                    )
                }
            }

            override suspend fun deleteList(list: ToDoList) {

            }

        }

        val toDoMainViewModel = ToDoMainViewModel(todoMainEnvironment)

        toDoMainViewModel.state.test {
            Assert.assertEquals(
                listOf(
                    ItemMainState.ItemGroup(
                        group = ToDoGroup(
                            id = "id",
                            name = "name",
                            lists = listOf(),
                            createdAt = DateFactory.constantDate,
                            updatedAt = DateFactory.constantDate,
                        )
                    )
                ),
                awaitItem().data
            )

            cancelAndConsumeRemainingEvents()
        }
    }

}
