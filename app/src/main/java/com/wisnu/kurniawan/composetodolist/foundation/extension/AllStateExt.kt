package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.features.todo.all.ui.ItemAllState

fun ItemAllState.identifier() = when (this) {
    is ItemAllState.Complete -> task.id
    is ItemAllState.InProgress -> task.id
    is ItemAllState.List -> list.id
}
