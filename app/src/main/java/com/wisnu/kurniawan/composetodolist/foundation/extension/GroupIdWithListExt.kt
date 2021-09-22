package com.wisnu.kurniawan.composetodolist.foundation.extension

import com.wisnu.kurniawan.composetodolist.foundation.datasource.local.model.ToDoGroupDb
import com.wisnu.kurniawan.composetodolist.model.GroupIdWithList

fun GroupIdWithList.isUngroup() = groupId == ToDoGroupDb.DEFAULT_ID

fun List<GroupIdWithList>.update(item: GroupIdWithList, groupId: String): List<GroupIdWithList> {
    return map {
        if (it.list.id == item.list.id) {
            val newGroupId = if (item.isUngroup()) {
                groupId
            } else {
                ToDoGroupDb.DEFAULT_ID
            }

            it.copy(
                groupId = newGroupId
            )
        } else {
            it
        }
    }
}
