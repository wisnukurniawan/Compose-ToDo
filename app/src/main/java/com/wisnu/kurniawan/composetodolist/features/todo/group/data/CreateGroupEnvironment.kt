package com.wisnu.kurniawan.composetodolist.features.todo.group.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.OnResolveDuplicateName
import com.wisnu.kurniawan.composetodolist.foundation.extension.duplicateNameResolver
import com.wisnu.kurniawan.composetodolist.foundation.extension.resolveGroupName
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeGenerator
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdGenerator
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class CreateGroupEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val localManager: LocalManager,
    override val idGenerator: IdGenerator,
    override val dateTimeGenerator: DateTimeGenerator
) : ICreateGroupEnvironment {

    override suspend fun getGroup(groupId: String): Flow<ToDoGroup> {
        return localManager.getGroup(groupId)
    }

    override suspend fun createGroup(name: String): Flow<String> {
        val listId = idGenerator.generate()
        val currentDate = dateTimeGenerator.now()
        val process: OnResolveDuplicateName = { newName ->
            localManager.insertGroup(
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
            onDuplicate = { resolveGroupName(name, localManager.getGroup(), process) }
        )
            .map { listId }
    }

    override suspend fun renameGroup(groupId: String, name: String): Flow<Any> {
        val currentDate = dateTimeGenerator.now()
        val process: OnResolveDuplicateName = { newName ->
            localManager.updateGroupName(groupId, newName, currentDate)
        }

        return duplicateNameResolver(
            updateName = { process(name) },
            onDuplicate = { resolveGroupName(name, localManager.getGroup(), process) },
        )
    }

}
