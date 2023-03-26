package com.wisnu.kurniawan.composetodolist.features.dashboard.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.features.dashboard.data.IDashboardEnvironment
import com.wisnu.kurniawan.composetodolist.features.todo.main.ui.ItemMainState
import com.wisnu.kurniawan.composetodolist.foundation.theme.MediumRadius
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
        viewModelScope.launch {
            environment.getUser()
                .collect { setState { copy(user = it) } }
        }
    }

    private fun initToDoTaskDiff() {
        viewModelScope.launch {
            environment.listenToDoTaskDiff()
                .collect()
        }
    }

    override fun dispatch(action: Unit) {

    }
}
