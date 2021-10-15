package com.wisnu.kurniawan.composetodolist.features.todo.detail.ui

sealed class ListDetailEffect {
    object ShowCreateListInput : ListDetailEffect()
    object ClosePage : ListDetailEffect()
    data class ScrollTo(val position: Int) : ListDetailEffect()
    data class Relaunch(val listId: String) : ListDetailEffect()
}
