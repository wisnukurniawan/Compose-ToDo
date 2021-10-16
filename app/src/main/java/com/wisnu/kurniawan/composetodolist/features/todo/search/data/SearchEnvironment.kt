package com.wisnu.kurniawan.composetodolist.features.todo.search.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.sanitizeQuery
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class SearchEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val localManager: LocalManager,
    private val dateTimeProvider: DateTimeProvider
) : ISearchEnvironment {

    override fun searchList(query: String): Flow<List<ToDoList>> {
        return if (query.isNotBlank()) {
            val wildcardQuery = query.sanitizeQuery()
            localManager.searchTaskWithList(wildcardQuery)
                .map {
                    it.groupBy { taskWithList -> taskWithList.list.id }
                        .map { (_, value) ->
                            val tasks = value.map { taskWithList -> taskWithList.task }
                            val list = value.map { taskWithList -> taskWithList.list }.first()
                            list.copy(tasks = tasks)
                        }
                        .sortedBy { task -> task.name }
                }
        } else {
            flow { emit(emptyList<ToDoList>()) }
        }
    }

    override suspend fun toggleTaskStatus(toDoTask: ToDoTask) {
        val currentDate = dateTimeProvider.now()
        toDoTask.toggleStatusHandler(
            currentDate,
            { completedAt, newStatus ->
                localManager.updateTaskStatus(toDoTask.id, newStatus, completedAt, currentDate)
            },
            { nextDueDate ->
                localManager.updateTaskDueDate(toDoTask.id, nextDueDate, toDoTask.isDueDateTimeSet, currentDate)
            }
        )
    }

    override suspend fun deleteTask(task: ToDoTask) {
        localManager.deleteTaskById(task.id)
    }
}
