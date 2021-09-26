package com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.model.ToDoStatus
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Named

class TaskReminderEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val localManager: LocalManager,
) : ITaskReminderEnvironment {

    override fun getTask(taskId: String): Flow<ToDoTask> {
        return localManager.getTaskWithStepsById(taskId)
            .take(1)
            .filter {
                it.status != ToDoStatus.COMPLETE &&
                    it.dueDate != null
            }
    }

}
