package com.wisnu.kurniawan.composetodolist.features.dashboard.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.datasource.preference.PreferenceManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import com.wisnu.kurniawan.composetodolist.model.ToDoTaskDiff
import com.wisnu.kurniawan.composetodolist.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class DashboardEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val preferenceManager: PreferenceManager,
    private val localManager: LocalManager,
) : IDashboardEnvironment {

    override fun getUser(): Flow<User> {
        return preferenceManager.getUser()
    }

    override fun listenToDoTaskDiff(): Flow<ToDoTaskDiff> {
        var tasks: Map<String, ToDoTask> = mapOf()
        return localManager.getTasksWithDueDate()
            .distinctUntilChangedBy { newTasks -> newTasks.map { Pair(it.dueDate, it.repeat) } } // Consume when due date and repeat have changes only
            .map { newTasks -> newTasks.associateBy({ it.id }, { it }) }
            .map { newTasks ->
                ToDoTaskDiff(
                    addedTask = newTasks - tasks.keys,
                    deletedTask = tasks - newTasks.keys,
                    modifiedTask = tasks.filter { (key, value) -> key in newTasks.keys && value != newTasks[key] }
                )
                    .apply {
                        tasks = newTasks
                    }
            }
            .drop(1) // Skip initial value
    }
}
