package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.dashboard.data.IDashboardEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data.TaskAlarmManager
import com.wisnu.kurniawan.composetodolist.features.todo.taskreminder.data.TaskNotificationManager
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
                .collect()
        }
    }

    override fun dispatch(action: Unit) {

    }
}
