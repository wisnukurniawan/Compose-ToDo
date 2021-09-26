package com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Named

class GroupMenuEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val localManager: LocalManager,
    override val dateTimeProvider: DateTimeProvider
) : IGroupMenuEnvironment {

    @OptIn(FlowPreview::class)
    override suspend fun deleteGroup(groupId: String): Flow<Any> {
        return hasList(groupId)
            .take(1)
            .onEach {
                if (it) {
                    localManager.deleteGroup(groupId)
                }
            }
            .filter { !it }
            .flatMapConcat { localManager.getListByGroupId(groupId).take(1) }
            .map { it.map { list -> list.id } }
            .onEach { localManager.ungroup(groupId, dateTimeProvider.now(), it) }
    }

    override fun hasList(groupId: String): Flow<Boolean> {
        return localManager.getListByGroupId(groupId).map { it.isEmpty() }
    }
}
