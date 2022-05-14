package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.main.data.IToDoMainEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_LIST_ID
import com.wisnu.kurniawan.composetodolist.runtime.navigation.AllFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ListDetailFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.MainFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ScheduledFlow
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ScheduledTodayFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoMainViewModel @Inject constructor(todoMainEnvironment: IToDoMainEnvironment) :
    StatefulViewModel<ToDoMainState, Unit, ToDoMainAction, IToDoMainEnvironment>(ToDoMainState(), todoMainEnvironment) {

    init {
        initToDo()
        initOverallCount()
    }

    override fun dispatch(action: ToDoMainAction) {
        when (action) {
            is ToDoMainAction.DeleteList -> {
                viewModelScope.launch {
                    environment.deleteList(action.itemListType.list)
                }
            }
            is ToDoMainAction.NavBackStackEntryChanged -> {
                viewModelScope.launch {
                    when (action.route) {
                        ListDetailFlow.ListDetailScreen.route -> {
                            val listId = action.arguments?.getString(ARG_LIST_ID)
                            if (listId.isNullOrBlank()) {
                                setState { copy(selectedItemState = SelectedItemState.Empty) }
                            } else {
                                setState { copy(selectedItemState = SelectedItemState.List(listId)) }
                            }
                        }
                        AllFlow.AllScreen.route -> {
                            setState { copy(selectedItemState = SelectedItemState.AllTask) }
                        }
                        ScheduledTodayFlow.ScheduledTodayScreen.route -> {
                            setState { copy(selectedItemState = SelectedItemState.ScheduledTodayTask) }
                        }
                        ScheduledFlow.ScheduledScreen.route -> {
                            setState { copy(selectedItemState = SelectedItemState.ScheduledTask) }
                        }
                        MainFlow.RootEmpty.route -> {
                            setState { copy(selectedItemState = SelectedItemState.Empty) }
                        }
                    }
                }
            }
        }
    }

    private fun initToDo() {
        viewModelScope.launch {
            environment.getGroup()
                .collect {
                    setState {
                        copy(
                            data = it,
                            currentDate = environment.dateTimeProvider.now().dayOfMonth.toString()
                        )
                    }
                }
        }
    }

    private fun initOverallCount() {
        viewModelScope.launch {
            environment.getOverallCount()
                .collect {
                    setState {
                        copy(
                            allTaskCount = it.allTaskCount.toString(),
                            scheduledTodayTaskCount = it.scheduledTodayTaskCount.toString(),
                            scheduledTaskCount = it.scheduledTaskCount.toString(),
                        )
                    }
                }
        }
    }

}
