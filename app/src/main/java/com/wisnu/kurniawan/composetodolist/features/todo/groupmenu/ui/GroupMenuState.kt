package com.wisnu.kurniawan.composetodolist.features.todo.groupmenu.ui

import androidx.compose.ui.graphics.vector.ImageVector
import javax.annotation.concurrent.Immutable

@Immutable
data class GroupMenuState(
    val items: List<GroupMenuItem> = listOf()
)

sealed class GroupMenuItem(open val title: Int) {
    data class AddRemove(override val title: Int, val enabled: Boolean) : GroupMenuItem(title)
    data class Rename(override val title: Int, val enabled: Boolean) : GroupMenuItem(title)
    data class Delete(override val title: Int, val enabled: Boolean, val icon: ImageVector) : GroupMenuItem(title)
}
