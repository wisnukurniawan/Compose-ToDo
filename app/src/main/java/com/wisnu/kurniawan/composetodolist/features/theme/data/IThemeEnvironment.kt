package com.wisnu.kurniawan.composetodolist.features.theme.data

import com.wisnu.kurniawan.composetodolist.model.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IThemeEnvironment {
    val dispatcher: CoroutineDispatcher
    fun getTheme(): Flow<Theme>
    suspend fun setTheme(theme: Theme)
}
