package com.wisnu.kurniawan.composetodolist.features.todo.search.ui

import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.search.data.ISearchEnvironment
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchEnvironment: ISearchEnvironment,
) :
    StatefulViewModel<SearchState, Unit, SearchAction, ISearchEnvironment>(SearchState(), searchEnvironment) {

    private var searchJob: Job? = null

    override fun dispatch(action: SearchAction) {
        when (action) {
            is SearchAction.ChangeSearchText -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    setState { copy(searchText = action.text) }
                    delay(250)
                    environment.searchList(action.text.text)
                        .collect {
                            setState { copy(lists = it) }
                        }
                }
            }
            is SearchAction.TaskAction.Delete -> {
                viewModelScope.launch {
                    environment.deleteTask(action.task)
                }
            }
            is SearchAction.TaskAction.OnToggleStatus -> {
                viewModelScope.launch {
                    environment.toggleTaskStatus(action.task)
                }
            }
        }
    }

}
