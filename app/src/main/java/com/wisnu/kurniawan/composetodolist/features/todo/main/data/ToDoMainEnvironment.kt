package com.wisnu.kurniawan.composetodolist.features.todo.main.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTaskOverallCount
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.LocalTime
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

    override fun getOverallCount(): Flow<ToDoTaskOverallCount> {
        val tomorrow = LocalDateTime.of(dateTimeProvider.now().toLocalDate().plusDays(1), LocalTime.MIN)
        return localManager.getOverallCount(tomorrow)
    }

    override suspend fun deleteList(list: ToDoList) {
        localManager.deleteListById(list.id)
    }

}
