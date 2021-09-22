package com.wisnu.kurniawan.composetodolist.features.todo.main.ui

sealed class ToDoMainAction {
    data class DeleteList(val itemListType: ItemMainState.ItemListType) : ToDoMainAction()
}
