package com.wisnu.kurniawan.composetodolist.features.todo.group.ui

sealed class CreateGroupEffect {
    object HideScreen : CreateGroupEffect()
    data class ShowUpdateGroupListScreen(val groupId: String) : CreateGroupEffect()
}
