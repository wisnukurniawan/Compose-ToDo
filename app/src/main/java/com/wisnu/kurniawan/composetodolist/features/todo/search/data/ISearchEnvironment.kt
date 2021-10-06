package com.wisnu.kurniawan.composetodolist.features.todo.search.data

import kotlinx.coroutines.CoroutineDispatcher

interface ISearchEnvironment {
    val dispatcher: CoroutineDispatcher
}
