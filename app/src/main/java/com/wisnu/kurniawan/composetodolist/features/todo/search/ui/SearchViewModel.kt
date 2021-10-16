package com.wisnu.kurniawan.composetodolist.features.todo.search.ui

import androidx.compose.ui.text.TextRange
import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.search.data.ISearchEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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
                searchJob = viewModelScope.launch(environment.dispatcher) {
                    setState { copy(searchText = action.text) }
                    delay(250)
                    environment.searchList(action.text.text)
                        .collect {
                            setState { copy(lists = it) }
                        }
                }
            }
            SearchAction.OnShow -> {
                viewModelScope.launch {
                    setState { copy(searchText = searchText.copy(selection = TextRange(searchText.text.length))) }
                }
            }
            is SearchAction.TaskAction.Delete -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.deleteTask(action.task)
                }
            }
            is SearchAction.TaskAction.OnToggleStatus -> {
                viewModelScope.launch(environment.dispatcher) {
                    environment.toggleTaskStatus(action.task)
                }
            }
        }
    }

}
