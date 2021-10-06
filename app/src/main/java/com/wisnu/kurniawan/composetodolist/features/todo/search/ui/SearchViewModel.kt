package com.wisnu.kurniawan.composetodolist.features.todo.search.ui

import androidx.compose.ui.text.TextRange
import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.search.data.ISearchEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchEnvironment: ISearchEnvironment,
) :
    StatefulViewModel<SearchState, Unit, SearchAction, ISearchEnvironment>(SearchState(), searchEnvironment) {

    override fun dispatch(action: SearchAction) {
        when (action) {
            is SearchAction.ChangeSearchText -> {
                viewModelScope.launch(environment.dispatcher) {
                    setState { copy(searchText = action.text) }
                }
            }
            SearchAction.OnShow -> {
                viewModelScope.launch {
                    setState { copy(searchText = searchText.copy(selection = TextRange(searchText.text.length))) }
                }
            }
        }
    }

}
