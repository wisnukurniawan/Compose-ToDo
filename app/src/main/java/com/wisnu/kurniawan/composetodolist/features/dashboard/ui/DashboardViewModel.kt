package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.dashboard.data.IDashboardEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.ui.TaskAlarmManager
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
                        LoggrDebug { "wsnukrn - Added task ${todoTaskDiff.addedTask}" }
                    }

                    if (todoTaskDiff.deletedTask.isNotEmpty()) {
                        LoggrDebug { "wsnukrn - Deleted task ${todoTaskDiff.deletedTask}" }
                    }

                    if (todoTaskDiff.modifiedTask.isNotEmpty()) {
                        LoggrDebug { "wsnukrn - Changed task ${todoTaskDiff.modifiedTask}" }
                    }

//                    todoTaskDiff.addedTask.forEach {
//                        taskAlarmManager.scheduleTaskAlarm(it.value, it.value.getScheduledDueDate(environment.currentDate()))
//                    }
//
//                    todoTaskDiff.modifiedTask.forEach {
//                        taskAlarmManager.scheduleTaskAlarm(it.value, it.value.getScheduledDueDate(environment.currentDate()))
//                    }
//
//                    todoTaskDiff.deletedTask.forEach {
//                        taskAlarmManager.cancelTaskAlarm(it.value)
//                    }
                }
        }
    }

    override fun dispatch(action: Unit) {

    }
}
