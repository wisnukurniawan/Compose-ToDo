package com.wisnu.kurniawan.composetodolist.features.todo.grouplist.ui

import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList

sealed class UpdateGroupListAction {

    data class Change(val item: GroupIdWithList) : UpdateGroupListAction()
    object Submit : UpdateGroupListAction()

}
