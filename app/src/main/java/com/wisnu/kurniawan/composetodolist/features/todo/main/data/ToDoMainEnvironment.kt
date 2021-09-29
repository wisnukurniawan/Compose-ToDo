package com.wisnu.kurniawan.composetodolist.features.todo.main.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class ToDoMainEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    override val dateTimeProvider: DateTimeProvider,
    private val localManager: LocalManager,
) : IToDoMainEnvironment {

    override fun getGroup(): Flow<List<ToDoGroup>> {
        return localManager.getGroupWithList()
    }

    override suspend fun deleteList(list: ToDoList) {
        localManager.deleteListById(list.id)
    }

}
