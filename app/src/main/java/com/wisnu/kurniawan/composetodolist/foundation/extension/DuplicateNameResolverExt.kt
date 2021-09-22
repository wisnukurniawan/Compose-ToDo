package com.wisnu.kurniawan.composetodolist.foundation.extension

import android.database.sqlite.SQLiteConstraintException
import com.wisnu.kurniawan.composetodolist.model.ToDoGroup
import com.wisnu.kurniawan.composetodolist.model.ToDoList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

typealias OnResolveDuplicateName = suspend (String) -> Unit

suspend fun duplicateNameResolver(
    updateName: suspend () -> Unit,
    onDuplicate: () -> Flow<Any>
): Flow<Any> {
    return try {
        updateName()
        flow { emit(Any()) }
    } catch (e: SQLiteConstraintException) {
        onDuplicate()
    }
}

fun resolveGroupName(
    name: String,
    getToDoGroups: Flow<List<ToDoGroup>>,
    updateNameWithNewName: OnResolveDuplicateName,
): Flow<Any> {
    return getToDoGroups
        .take(1)
        .map { group -> group.map { it.name } }
        .map { names -> name.resolveDuplicate(names) }
        .onEach { updateNameWithNewName(it) }
        .map { Any() }
}

fun resolveListName(
    name: String,
    getToDoLists: Flow<List<ToDoList>>,
    updateNameWithNewName: OnResolveDuplicateName,
): Flow<Any> {
    return getToDoLists
        .take(1)
        .map { list -> list.map { it.name } }
        .map { names -> name.resolveDuplicate(names) }
        .onEach { updateNameWithNewName(it) }
        .map { Any() }
}

fun String.addSuffixIdentifier(): String {
    if (isBlank()) return "1"

    val names = this.split(" ")

    if (names.size == 1) return "$this 1"

    val currentIdentifier = names.last().toIntOrNull() ?: return "$this 1"

    val lastIndex = names.lastIndex

    return names.foldIndexed(
        "",
        { index, acc, cur ->
            when {
                index == lastIndex -> acc + " ${currentIdentifier + 1}"
                acc.isBlank() -> cur
                else -> "$acc $cur"
            }
        }
    )
}

fun String.resolveDuplicate(names: List<String>): String {
    return if (names.any { it == this }) {
        this.addSuffixIdentifier().resolveDuplicate(names)
    } else {
        this
    }
}
