package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.main.data.IToDoMainEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.extension.toItemGroup
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
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
                viewModelScope.launch(environment.dispatcher) {
                    environment.deleteList(action.itemListType.list)
                }
            }
        }
    }

    private fun initToDo() {
        viewModelScope.launch {
            environment.getGroup()
                .flowOn(environment.dispatcher)
                .collect {
                    setState {
                        copy(
                            data = it.toItemGroup(),
                            currentDate = environment.dateTimeProvider.now().dayOfMonth.toString()
                        )
                    }
                }
        }
    }

    private fun initOverallCount() {
        viewModelScope.launch {
            environment.getOverallCount()
                .flowOn(environment.dispatcher)
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
