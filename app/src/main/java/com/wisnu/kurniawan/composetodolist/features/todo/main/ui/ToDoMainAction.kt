package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

import android.os.Bundle

sealed class ToDoMainAction {
    data class DeleteList(val itemListType: ItemMainState.ItemListType) : ToDoMainAction()
    data class NavBackStackEntryChanged(
        val route: String?,
        val arguments: Bundle?
    ) : ToDoMainAction()
}
