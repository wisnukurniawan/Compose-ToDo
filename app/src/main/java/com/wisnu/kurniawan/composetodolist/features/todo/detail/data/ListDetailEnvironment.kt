package com.wisnu.kurniawan.composetodolist.features.todo.detail.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.OnResolveDuplicateName
import com.wisnu.kurniawan.composetodolist.foundation.extension.duplicateNameResolver
import com.wisnu.kurniawan.composetodolist.foundation.extension.getNextScheduledDueDate
import com.wisnu.kurniawan.composetodolist.foundation.extension.newStatus
import com.wisnu.kurniawan.composetodolist.foundation.extension.resolveListName
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdGenerator
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoRepeat
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject
import javax.inject.Named

@FlowPreview
class ListDetailEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val localManager: LocalManager,
    override val idGenerator: IdGenerator,
    override val dateTimeGenerator: DateTimeGenerator,
) : IListDetailEnvironment {

    override fun getListWithTasksById(listId: String): Flow<ToDoList> {
        return localManager.getListWithTasksById(listId)
    }

    override suspend fun createList(list: ToDoList): Flow<ToDoList> {
        val process: OnResolveDuplicateName = { newName ->
            localManager.insertList(
                listOf(list.copy(name = newName)),
                ToDoGroupDb.DEFAULT_ID
            )
        }

        return duplicateNameResolver(
            updateName = { process(list.name) },
            onDuplicate = { resolveListName(list.name, localManager.getList(), process) }
        )
            .flatMapConcat { localManager.getListWithTasksById(list.id) }
    }

    override suspend fun updateList(list: ToDoList): Flow<Any> {
        val currentDate = dateTimeGenerator.now()
        val process: OnResolveDuplicateName = { newName ->
            localManager.updateListNameAndColor(list.copy(name = newName), currentDate)
        }

        return duplicateNameResolver(
            updateName = { process(list.name) },
            onDuplicate = { resolveListName(list.name, localManager.getList(), process) }
        )
    }

    override suspend fun createTask(taskName: String, listId: String) {
        val currentDate = dateTimeGenerator.now()

        localManager.insertTask(
            listOf(
                ToDoTask(
                    id = idGenerator.generate(),
                    name = taskName,
                    createdAt = currentDate,
                    updatedAt = currentDate
                )
            ),
            listId
        )
    }

    override suspend fun toggleTaskStatus(toDoTask: ToDoTask) {
        val currentDate = dateTimeGenerator.now()
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
