package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.group.ui.CreateGroupState

fun CreateGroupState.isValidGroupName() = groupName.text.isNotBlank()

