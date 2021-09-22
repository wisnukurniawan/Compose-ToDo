package com.wisnu.kurniawan.composetodolist.foundation.extension

import androidx.compose.ui.graphics.Color
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListBrown
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListGreen
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListOrange
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListPurple
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListRed
import com.wisnu.kurniawan.composetodolist.foundation.theme.ListYellow
import com.wisnu.kurniawan.composetodolist.model.ToDoColor

fun Color.toToDoColor() = when (this) {
    ListRed -> ToDoColor.RED
    ListGreen -> ToDoColor.GREEN
    ListYellow -> ToDoColor.YELLOW
    ListOrange -> ToDoColor.ORANGE
    ListPurple -> ToDoColor.PURPLE
    ListBrown -> ToDoColor.BROWN
    else -> ToDoColor.BLUE
}
