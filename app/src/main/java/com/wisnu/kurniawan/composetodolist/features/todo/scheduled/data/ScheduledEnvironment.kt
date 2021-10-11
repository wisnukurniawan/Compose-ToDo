package com.wisnu.kurniawan.composetodolist.features.todo.scheduled.data

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import com.wisnu.kurniawan.composetodolist.foundation.extension.toggleStatusHandler
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.composetodolist.foundation.wrapper.IdProvider
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import com.wisnu.kurniawan.composetodolist.model.ToDoTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

@OptIn(FlowPreview::class)
class ScheduledEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
    private val localManager: LocalManager,
    override val idProvider: IdProvider,
    override val dateTimeProvider: DateTimeProvider,
) : IScheduledEnvironment {

    override fun getToDoTaskWithStepsOrderByDueDateWithList(maxDate: LocalDateTime?): Flow<List<Pair<ToDoTask, ToDoList>>> {
        return localManager.getList()
            .map { lists -> lists.associateBy({ it.id }, { it }) }
            .flatMapConcat { lists ->
                val operation = if (maxDate != null) {
                    localManager.getToDoTaskWithStepsOrderByDueDateTodayWithListId(maxDate)
                } else {
                    localManager.getToDoTaskWithStepsOrderByDueDateWithListId()
                }
                operation
                    .map {
                        it.map { task -> Pair(task.task, lists[task.listId]!!) }
                    }
            }
            .distinctUntilChanged()
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
