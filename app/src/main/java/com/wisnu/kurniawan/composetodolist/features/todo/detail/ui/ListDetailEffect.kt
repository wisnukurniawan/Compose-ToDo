package com.wisnu.kurniawan.composetodolist.features.todo.detail.ui

sealed class ListDetailEffect {
    object ShowCreateListInput : ListDetailEffect()
    object ClosePage : ListDetailEffect()

}
