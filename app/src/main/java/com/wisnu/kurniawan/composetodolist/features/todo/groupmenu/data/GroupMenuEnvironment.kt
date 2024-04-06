package com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoGroupProvider
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.provider.ToDoListProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GroupMenuEnvironment @Inject constructor(
    private val toDoGroupProvider: ToDoGroupProvider,
    private val toDoListProvider: ToDoListProvider,
    override val dateTimeProvider: DateTimeProvider
) : IGroupMenuEnvironment {

    override suspend fun deleteGroup(groupId: String): Flow<Any> {
        return hasList(groupId)
            .take(1)
            .onEach {
                if (it) {
                    toDoGroupProvider.deleteGroup(groupId)
                }
            }
            .filter { !it }
            .flatMapConcat { toDoListProvider.getListByGroupId(groupId).take(1) }
            .map { it.map { list -> list.id } }
            .onEach { toDoGroupProvider.ungroup(groupId, dateTimeProvider.now(), it) }
    }

    override fun hasList(groupId: String): Flow<Boolean> {
        return toDoListProvider.getListByGroupId(groupId).map { it.isEmpty() }
    }
}
