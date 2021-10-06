package com.wisnu.kurniawan.composetodolist.features.todo.search.data

import com.wisnu.kurniawan.composetodolist.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class SearchEnvironment @Inject constructor(
    @Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
) : ISearchEnvironment {

}
