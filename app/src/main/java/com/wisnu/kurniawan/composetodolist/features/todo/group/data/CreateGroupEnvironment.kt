package com.wisnu.kurniawan.composetodolist.features.todo.group.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoGroupProvider
import com.wisnu.kurniawan.composetodolist.foundation.extension.OnResolveDuplicateName
import com.wisnu.kurniawan.composetodolist.foundation.extension.duplicateNameResolver
import com.wisnu.kurniawan.composetodolist.foundation.extension.resolveGroupName
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CreateGroupEnvironment @Inject constructor(
    private val toDoGroupProvider: ToDoGroupProvider,
    override val idProvider: IdProvider,
    override val dateTimeProvider: DateTimeProvider
) : ICreateGroupEnvironment {

    override suspend fun getGroup(groupId: String): Flow<ToDoGroup> {
        return toDoGroupProvider.getGroup(groupId)
    }

    override suspend fun createGroup(name: String): Flow<String> {
        val listId = idProvider.generate()
        val currentDate = dateTimeProvider.now()
        val process: OnResolveDuplicateName = { newName ->
            toDoGroupProvider.insertGroup(
                listOf(
                    ToDoGroup(
                        id = listId,
                        name = newName,
                        createdAt = currentDate,
                        updatedAt = currentDate
                    )
                )
            )
        }

        return duplicateNameResolver(
            updateName = { process(name) },
            onDuplicate = { resolveGroupName(name, toDoGroupProvider.getGroup(), process) }
        )
            .map { listId }
    }

    override suspend fun renameGroup(groupId: String, name: String): Flow<Any> {
        val currentDate = dateTimeProvider.now()
        val process: OnResolveDuplicateName = { newName ->
            toDoGroupProvider.updateGroupName(groupId, newName, currentDate)
        }

        return duplicateNameResolver(
            updateName = { process(name) },
            onDuplicate = { resolveGroupName(name, toDoGroupProvider.getGroup(), process) },
        )
    }

}
