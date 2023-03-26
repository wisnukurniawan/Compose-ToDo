package com.wisnu.kurniawan.composetodolist.features.todo.grouplist.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.features.todo.grouplist.data.IUpdateGroupListEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_GROUP_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateGroupListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    updateGroupListEnvironment: IUpdateGroupListEnvironment,
) :
    StatefulViewModel<UpdateGroupListState, Unit, UpdateGroupListAction, IUpdateGroupListEnvironment>(UpdateGroupListState(), updateGroupListEnvironment) {

    private val groupId = savedStateHandle.get<String>(ARG_GROUP_ID)

    init {
        viewModelScope.launch {
            if (!groupId.isNullOrBlank()) {
                delay(100)
                environment.getListWithUnGroupList(groupId).collect {
                    setState {
                        copy(
                            initialItems = it,
                            items = it
                        )
                    }
                }
            }
        }
    }

    override fun dispatch(action: UpdateGroupListAction) {
        when (action) {
            UpdateGroupListAction.Submit -> {
                viewModelScope.launch {
                    delay(100)
                    val data = state.value.items.filter { !state.value.initialItems.contains(it) }
                    environment.updateList(data)
                }
            }
            is UpdateGroupListAction.Change -> {
                viewModelScope.launch {
                    delay(100)
                    setState { copy(items = items.update(action.item, groupId.orEmpty())) }
                }
            }
        }
    }

}

fun GroupIdWithList.isUngroup() = groupId == ToDoGroupDb.DEFAULT_ID

fun List<GroupIdWithList>.update(item: GroupIdWithList, groupId: String): List<GroupIdWithList> {
    return map {
        if (it.list.id == item.list.id) {
            val newGroupId = if (item.isUngroup()) {
                groupId
            } else {
                ToDoGroupDb.DEFAULT_ID
            }

            it.copy(
                groupId = newGroupId
            )
        } else {
            it
        }
    }
}

