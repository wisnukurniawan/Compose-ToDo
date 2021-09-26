package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.dashboard.data.IDashboardEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.ui.TaskAlarmManager
import com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.ui.TaskNotificationManager
import com.wisnu.kurniawan.composetodolist.foundation.extension.getScheduledDueDate
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import com.wisnu.kurniawan.coreLogger.LoggrDebug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    dashboardEnvironment: IDashboardEnvironment,
    private val taskAlarmManager: TaskAlarmManager,
    private val notificationManager: TaskNotificationManager
) :
    StatefulViewModel<DashboardState, Unit, Unit, IDashboardEnvironment>(DashboardState(), dashboardEnvironment) {

    init {
        initUser()
        initToDoTaskDiff()
    }

    private fun initUser() {
        viewModelScope.launch(environment.dispatcher) {
            environment.getUser()
                .collect { setState { copy(user = it) } }
        }
    }

    private fun initToDoTaskDiff() {
        viewModelScope.launch(environment.dispatcher) {
            environment.listenToDoTaskDiff()
                .collect { todoTaskDiff ->
                    if (todoTaskDiff.addedTask.isNotEmpty()) {
                        LoggrDebug { "DashboardViewModel - Added task ${todoTaskDiff.addedTask}" }
                    }

                    if (todoTaskDiff.deletedTask.isNotEmpty()) {
                        LoggrDebug { "DashboardViewModel - Deleted task ${todoTaskDiff.deletedTask}" }
                    }

                    if (todoTaskDiff.modifiedTask.isNotEmpty()) {
                        LoggrDebug { "DashboardViewModel - Changed task ${todoTaskDiff.modifiedTask}" }
                    }

                    todoTaskDiff.addedTask.forEach {
                        taskAlarmManager.scheduleTaskAlarm(it.value, it.value.getScheduledDueDate(environment.dateTimeProvider.now()))
                    }

                    todoTaskDiff.modifiedTask.forEach {
                        taskAlarmManager.scheduleTaskAlarm(it.value, it.value.getScheduledDueDate(environment.dateTimeProvider.now()))
                    }

                    todoTaskDiff.deletedTask.forEach {
                        taskAlarmManager.cancelTaskAlarm(it.value)
                        notificationManager.dismiss(it.value)
                    }
                }
        }
    }

    override fun dispatch(action: Unit) {

    }
}
