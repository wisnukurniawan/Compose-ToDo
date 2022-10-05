package com.wisnu.kurniawan.composetodolist.features.todo.group.ui

import androidx.compose.ui.text.TextRange
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.wisnu.kurniawan.composetodolist.features.todo.group.data.ICreateGroupEnvironment
import com.wisnu.kurniawan.composetodolist.foundation.extension.isValidGroupName
import com.wisnu.foundation.coreviewmodel.StatefulViewModel
import com.wisnu.kurniawan.composetodolist.runtime.navigation.ARG_GROUP_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    createGroupEnvironment: ICreateGroupEnvironment,
) :
    StatefulViewModel<CreateGroupState, CreateGroupEffect, CreateGroupAction, ICreateGroupEnvironment>(CreateGroupState(), createGroupEnvironment) {

    private val groupId = savedStateHandle.get<String>(ARG_GROUP_ID)

    init {
        viewModelScope.launch {
            if (!groupId.isNullOrBlank()) {
                environment.getGroup(groupId)
                    .collect {
                        setState {
                            copy(
                                groupName = groupName.copy(
                                    text = it.name,
                                    selection = TextRange(it.name.length)
                                )
                            )
                        }
                    }
            }
        }
    }

    override fun dispatch(action: CreateGroupAction) {
        when (action) {
            is CreateGroupAction.ChangeGroupName -> {
                viewModelScope.launch {
                    setState { copy(groupName = action.name) }
                }
            }
            CreateGroupAction.ClickImeDone, CreateGroupAction.ClickSave -> {
                viewModelScope.launch {
                    if (state.value.isValidGroupName()) {
                        if (!groupId.isNullOrBlank()) {
                            environment.renameGroup(groupId, state.value.groupName.text.trim()).collect {
                                setEffect(CreateGroupEffect.HideScreen)
                            }
                        } else {
                            environment.createGroup(state.value.groupName.text.trim()).collect {
                                setEffect(CreateGroupEffect.ShowUpdateGroupListScreen(it))
                            }
                        }
                    }
                }
            }
            is CreateGroupAction.InitName -> {
                viewModelScope.launch {
                    setState { copy(groupName = action.name) }
                }
            }
        }
    }
}
