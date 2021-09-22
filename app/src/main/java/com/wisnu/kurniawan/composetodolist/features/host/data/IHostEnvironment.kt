package com.wisnu.kurniawan.composetodolist.features.host.data

import com.wisnu.kurniawan.composetodolist.model.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IHostEnvironment {
    val dispatcher: CoroutineDispatcher
    fun getTheme(): Flow<Theme>
}
